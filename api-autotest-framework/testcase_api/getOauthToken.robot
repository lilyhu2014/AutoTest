*** Settings ***
Resource   ..${/}robotkeywords${/}Common.robot

Test Template   Get-Oauth-Token

*** Test Cases ***         expected_status_code
call_success      200
    
*** Keywords ***
Get-Oauth-Token
    [Arguments]   ${expected_status_code}
    ${api_url} =  Set Variable  https://prod-tftv-oauth.tokenpad.io/oauth/token
    ${header} =  Create Dictionary   
    ...   Authorization=Basic Y2xpZW50OnNlY3JldA==  
    ...   Content-Type=application/x-www-form-urlencoded
    ${requestBody} =  Set Variable  username=18349154571&password=112233&grant_type=password
    ${result} =  Invoke Service  POST  ${header}  ${api_url}  ${requestBody}
    Should Be Equal As Integers   ${result.statusCode}    ${expected_status_code}