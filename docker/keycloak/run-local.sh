#!/bin/zsh

if [[ -z "$KC_PATH" ]]; then
  echo "Please add this line\n\nexport KC_PATH=<path to your keycloak installation>\n\nto your bash profile (e.g. ~/.bashrc)"
  exit
fi

if [[ -f "${KC_PATH}/.env" ]]; then
  echo "source \"${KC_PATH}/.env\";"
  source "${KC_PATH}/.env";
else
  echo "KC_DB_URL=\nKC_DB_USERNAME=\nKC_DB_PASSWORD=\nKC_KEYSTORE_FILE=\nKC_KEYSTORE_PASSWORD=\nKC_HOSTNAME=" > "${KC_PATH}/.env"
fi

if [[ -z "$KC_DB_URL" || -z "$KC_DB_USERNAME" || -z "$KC_DB_PASSWORD" || -z "$KC_KEYSTORE_FILE" || -z "$KC_KEYSTORE_PASSWORD" || -z "${KC_HOSTNAME}" ]]; then
  echo "Please setup .env file on your keycloak folder, and fill these following variables!"
  cat "${KC_PATH}/.env"
  exit 0
fi

if [[ -f "${KC_PATH}/bin/kc.sh" ]]; then
  echo "WORKDIR \"${KC_PATH}\""
  echo "Running keycloak!"
  WORKDIR "${KC_PATH}"
  ${KC_PATH}/bin/kc.sh start \
--optimized \
--db-url=${KC_DB_URL} \
--db-username=${KC_DB_USERNAME} \
--db-password=${KC_DB_PASSWORD} \
--https-key-store-file=${KC_KEYSTORE_FILE} \
--https-key-store-password=${KC_KEYSTORE_PASSWORD} \
--proxy-headers=forwarded \
--hostname=https://${KC_HOSTNAME}

else
  echo "${KC_PATH}/bin/kc.sh doesn't exists!"
  echo "${KC_PATH} is not a valid keycloak installation! Please visit https://www.keycloak.org/getting-started/getting-started-zip"
fi