;Fakturama Installer
  !define VERSION "1.6.6"
  OutFile "Fakturama_Win_32Bit_${VERSION}.exe"

;--------------------------------
;Version Information

  VIProductVersion "${VERSION}.0"
  VIAddVersionKey /LANG=${LANG_ENGLISH} "ProductName" "Fakturama installation file"
  VIAddVersionKey /LANG=${LANG_ENGLISH} "CompanyName" "Fakturama.org"
  VIAddVersionKey /LANG=${LANG_ENGLISH} "LegalCopyright" "Copyright Fakturama.org"
  VIAddVersionKey /LANG=${LANG_ENGLISH} "FileDescription" "Fakturama installer"
  VIAddVersionKey /LANG=${LANG_ENGLISH} "FileVersion" "${VERSION}"

  
  InstallDir "$PROGRAMFILES\Fakturama"
  !include "general.nsi"
  