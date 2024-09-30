#!/bin/zsh

if [[ -z "$KC_PATH" ]]; then
  echo "Please add this line\n\nexport KC_PATH=<path to your keycloak installation>\n\nto your bash profile (e.g. ~/.bashrc)"
  exit
fi

if [[ -f "${KC_PATH}/bin/kc.sh" ]]; then
  echo "WORKDIR ${KC_PATH}"
  echo "${KC_PATH}/bin/kc.sh build --features=\"docker,token-exchange\""

  WORKDIR "${KC_PATH}"
  ${KC_PATH}/bin/kc.sh build --features="docker,token-exchange"
else
  echo "${KC_PATH} is not a valid keycloak installation! Please visit https://www.keycloak.org/getting-started/getting-started-zip"
fi