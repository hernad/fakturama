;NSIS Modern User Interface
;Fakturama Installer

;--------------------------------
;Include Modern UI

  !include "MUI2.nsh"

;--------------------------------
;General

  ;Name and file
  Name "Fakturama ${VERSION}"
  
  BRANDINGTEXT "(C) Ralf Heydenreich 2014"

  ;Get installation folder from registry if available
  ;InstallDirRegKey HKCU "Software\Fakturama" ""

  ;Request application privileges for Windows Vista
  RequestExecutionLevel admin

  !define MUI_ICON "Fakturama.ico"
  !define MUI_UNICON "Fakturama.ico"


;--------------------------------
;Interface Configuration

  !define MUI_HEADERIMAGE
  !define MUI_HEADERIMAGE_BITMAP "fakturama_logo.bmp" ;
  !define MUI_ABORTWARNING

;--------------------------------
;Language Selection Dialog Settings

  ;Remember the installer language
  !define MUI_LANGDLL_REGISTRY_ROOT "HKCU" 
  !define MUI_LANGDLL_REGISTRY_KEY "Software\Fakturama" 
  !define MUI_LANGDLL_REGISTRY_VALUENAME "Installer Language"

;--------------------------------
;Pages

  !insertmacro MUI_PAGE_LICENSE "epl-v10.txt"
; !insertmacro MUI_PAGE_COMPONENTS
  !insertmacro MUI_PAGE_DIRECTORY
  !insertmacro MUI_PAGE_INSTFILES
  
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES
  !define MUI_FINISHPAGE_RUN "$INSTDIR\Fakturama.exe"
  
;--------------------------------
;Languages
 
  !insertmacro MUI_LANGUAGE "English" ;first language is the default language
  !insertmacro MUI_LANGUAGE "German"

;--------------------------------
;Reserve Files
  
  ;If you are using solid compression, files that are required before
  ;the actual installation should be stored first in the data block,
  ;because this will make your installer start faster.
  
  !insertmacro MUI_RESERVEFILE_LANGDLL

;--------------------------------
;Installer Sections

Section "Fakturama" SecFakturama

  SetOutPath "$INSTDIR"
  
  ;ADD YOUR OWN FILES HERE...
  File /r Fakturama\*.*
  
  ;Store installation folder
  WriteRegStr HKCU "Software\Fakturama" "" $INSTDIR
  
  ;Create uninstaller
  WriteUninstaller "$INSTDIR\Uninstall.exe"

  ; create a shortcut named "new shortcut" in the start menu programs directory
  createShortCut "$SMPROGRAMS\Fakturama.lnk" "$INSTDIR\Fakturama.exe"

  ;create desktop shortcut
  CreateShortCut "$DESKTOP\Fakturama.lnk" "$INSTDIR\Fakturama.exe" ""

;write uninstall information to the registry
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Fakturama" "DisplayName" "Fakturama"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Fakturama" "UninstallString" "$INSTDIR\Uninstall.exe"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Fakturama" "Publisher" "sebulli.com"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Fakturama" "DisplayVersion" "${VERSION}"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Fakturama" "DisplayIcon" "$INSTDIR\Uninstall.exe,0"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Fakturama" "HelpLink" "http://fakturama.sebulli.com"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Fakturama" "URLInfoAbout" "http://fakturama.sebulli.com"

# Make the directory "$INSTDIR\database" read write accessible by all users
  AccessControl::GrantOnFile \
    "$INSTDIR" "(BU)" "FullAccess"  

SectionEnd

;--------------------------------
;Descriptions

  ;USE A LANGUAGE STRING IF YOU WANT YOUR DESCRIPTIONS TO BE LANGAUGE SPECIFIC

  ;Assign descriptions to sections
  !insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
    !insertmacro MUI_DESCRIPTION_TEXT ${SecFakturama} "Fakturama"
  !insertmacro MUI_FUNCTION_DESCRIPTION_END

 
;--------------------------------
;Uninstaller Section

Section "Uninstall"

  ;ADD YOUR OWN FILES HERE...

  Delete "$INSTDIR\Uninstall.exe"

  RMDir /r "$INSTDIR"

  ;Delete Uninstaller And Unistall Registry Entries
  DeleteRegKey HKEY_LOCAL_MACHINE "SOFTWARE\Fakturama"
  DeleteRegKey HKEY_LOCAL_MACHINE "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\Fakturama"
  DeleteRegKey /ifempty HKCU "Software\Fakturama"

  # second, remove the link from the start menu
  delete "$SMPROGRAMS\Fakturama.lnk"

  # remove the link from the desktop
  delete "$DESKTOP\Fakturama.lnk"


SectionEnd