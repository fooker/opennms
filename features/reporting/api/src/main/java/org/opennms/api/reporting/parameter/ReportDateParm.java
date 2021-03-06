/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2005-2020 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2020 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.api.reporting.parameter;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import org.opennms.api.reporting.ReportMode;

/**
 * <p>ReportDateParm class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
public class ReportDateParm extends ReportParm implements Serializable {
    private static final long serialVersionUID = -8528562178984136887L;

    private Date m_date;
    private Boolean m_useAbsoluteDate;
    private String m_interval;
    private Integer m_count;
    private Integer m_hours;
    private Integer m_minutes;
    private boolean m_isAdjustedDate;
    
    public Boolean getUseAbsoluteDate() {
        return m_useAbsoluteDate;
    }

    public void setUseAbsoluteDate(Boolean useAbsoluteDate) {
        m_useAbsoluteDate = useAbsoluteDate;
    }

    public String getInterval() {
        return m_interval;
    }

    public void setInterval(String interval) {
        m_interval = interval;
    }

    public Integer getCount() {
        return m_count;
    }

    public void setCount(Integer count) {
        m_count = count;
    }
    
    public Date getDate() {
        return m_date;
    }

    public void setDate(Date date) {
        m_date = date;
    }

    public Date getValue(ReportMode mode) {
        if ((mode == ReportMode.SCHEDULED) && (m_useAbsoluteDate == false)) {
            final Calendar cal = Calendar.getInstance();
            // use the offset date set when the report was scheduled
            int amount = 0 - m_count;
            if (m_interval.equals("year")) {
                cal.add(Calendar.YEAR, amount);
            } else { 
                if (m_interval.equals("month")) {
                    cal.add(Calendar.MONTH, amount);
                } else {
                    cal.add(Calendar.DATE, amount);
                }
            }
            if (m_hours != null) {
                cal.set(Calendar.HOUR_OF_DAY, m_hours);
            } else {
                cal.set(Calendar.HOUR_OF_DAY, 0);
            }
            if (m_minutes != null) {
                cal.set(Calendar.MINUTE, m_minutes);
            } else {
                cal.set(Calendar.MINUTE, 0);
            }
            cal.set(Calendar.SECOND,0);
            cal.set(Calendar.MILLISECOND,0);
            return cal.getTime();
        }

        return this.getAdjustedDate();
    }

    public Integer getHours() {
        return m_hours;
    }

    public void setHours(Integer hour) {
        m_hours = hour;
    }

    public Integer getMinutes() {
        return m_minutes;
    }

    public void setMinutes(Integer minute) {
        m_minutes = minute;
    }

    @Override
    void accept(ReportParmVisitor visitor) {
        Objects.requireNonNull(visitor).visit(this);
    }

    public boolean isAdjustedDate() {
        return m_isAdjustedDate;
    }

    public void setIsAdjustedDate(final boolean adjusted) {
        m_isAdjustedDate = adjusted;
    }

    private Date getAdjustedDate() {
        if (m_isAdjustedDate) {
            return m_date;
        }

        long millis = m_date.getTime();
        if (!m_isAdjustedDate) {
            if (m_hours != null) {
                millis += (m_hours * 60 * 60 * 1000);
            }
            if (m_minutes != null) {
                millis += (m_minutes * 60 * 1000);
            }
        }
        return new Date(millis);
    }

}
