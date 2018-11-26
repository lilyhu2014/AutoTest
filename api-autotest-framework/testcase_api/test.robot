*** Settings ***
Resource   ..${/}robotkeywords${/}Common.robot

Test Template   Get-categories

*** Test Cases ***         expected_status_code
call_success      200
    
*** Keywords ***
Get-categories
    [Arguments]   ${expected_status_code}
    ${api_url}=  Set Variable  https://api.tokenpad.io/platform/v1/ico/categories
    ${result}=  Invoke Service  GET  ${api_url}
    Should Be Equal As Integers   ${result.statusCode}    ${expected_status_code}