#!/bin/sh -e

FORCE=0

if [ -n "$1" ] && [ "$1" = "-f" ]; then
  FORCE=1
fi

MYDIR="$(dirname "$0")"
MYDIR="$(cd "$MYDIR" || exit 1; pwd)"

# shellcheck disable=SC1090
if [ -e "${MYDIR}/_lib.sh" ]; then
  # shellcheck disable=SC1090,SC1091
  . "${MYDIR}/_lib.sh"
fi

if [ -z "${OPENNMS_HOME}" ]; then
  # shellcheck disable=SC2154
  OPENNMS_HOME="${install.dir}"
fi

if [ -e "${OPENNMS_HOME}/etc/opennms.conf" ]; then
  # shellcheck disable=SC1090,SC1091
  . "${OPENNMS_HOME}/etc/opennms.conf"
fi

if [ -z "${RUNAS}" ]; then
  echo "Something is very wrong, \$RUNAS is not set. Bailing."
  exit 1
fi

if [ "${RUNAS}" != "opennms" ] && [ "${FORCE}" -eq 0 ]; then
  echo "\$RUNAS has been changed from the default 'opennms' user. Assuming you know what you're doing and _not_ changing ownership of any files."
  exit 0
fi

RUNAS_UID="$(id -u "${RUNAS}" 2>/dev/null || :)"
RUNAS_GID="$(id -g "${RUNAS}" 2>/dev/null || :)"

if [ -z "${RUNAS_UID}" ] || [ -z "${RUNAS_GID}" ]; then
  echo "ERROR: unable to determine UID and GID from \$RUNAS=${RUNAS}. Bailing."
  exit 1
fi

SUDO="$(which sudo 2>/dev/null)"

do_chown() {
  _mypath="$1"

  if [ -x "$SUDO" ]; then
    "$SUDO" chown "${RUNAS_UID}:${RUNAS_GID}" "${_mypath}"
  else
    chown "${RUNAS_UID}:${RUNAS_GID}" "${_mypath}"
  fi
}

recursive_chown() {
  _mydir="$1"

  if [ ! -d "${_mydir}" ]; then
    return 0
  fi

  # just in case :)
  if [ "${_mydir}" = "/" ]; then
    echo "ERROR: trying to chown / -- something is probably very broken. Bailing."
    return 1
  fi

  if [ -d "${_mydir}" ]; then
    echo "* fixing ownership of ${_mydir}"
    if [ -x "$SUDO" ]; then
      find -x "${_mydir}" ! -user "${RUNAS_UID}" -print0 | "$SUDO" xargs -0 chown "${RUNAS_UID}:${RUNAS_GID}"
    else
      find -x "${_mydir}" ! -user "${RUNAS_UID}" -print0 | xargs -0 chown "${RUNAS_UID}:${RUNAS_GID}"
    fi
  else
    echo "! skipping ${_mydir} -- it is not a directory"
  fi
}

recursive_chown "${OPENNMS_HOME}/"
find "${OPENNMS_HOME}"/* -type l -maxdepth 0 | while read -r SUBDIR; do
  recursive_chown "${SUBDIR}/"
done

for TOOL in opennms minion sentinel; do
  for TOOLFILE in "/etc/default/${TOOL}" "/etc/sysconfig/${TOOL}"; do
    if [ -e "${TOOLFILE}" ]; then
      echo "* fixing ownership of ${TOOLFILE}"
      do_chown "${TOOLFILE}"
    fi
  done
done
