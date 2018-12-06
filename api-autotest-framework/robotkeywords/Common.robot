*** Setting ***
Library        api.autotest.framework.BasicLibrary
Library        DateTime
Library        String
Library        Collections

*** Variables ***
${oauthUser}           client
${oauthPassword}       secret
${api_oauth_url}       https://prod-tftv-oauth.tokenpad.io/oauth/token

${adminUser}           18349154571
${adminPassword}       112233

${base_api_url}        https://prod-tftv-mall.tokenpad.io

*** Keywords ***
Connect Database
  [Documentation]  Keyword to establish database connection for promo2 database
  Connect To Database  TIANFU   com.mysql.jdbc.Driver   jdbc:mysql://dev-tftv-mysql01.tokenpad.io:3306/tianfu?useUnicode=true&characterEncoding=UTF-8   tftv   tftv@tianfu

Disconnect Database
  [Documentation]  Keyword to disconnect database connection for promo2 database
  Disconnect From Database  TIANFU
  
Use Admin Account
    Get Request Headers  ${adminUser}  ${adminPassword}
    
Get Access Token
    [Documentation]    Get the oauth token and save to cache.
    [Arguments]   ${user}    ${password}
    ${key} =  Encode Base64  ${oauthUser}:${oauthPassword}
    ${request_headers} =  Create Dictionary  Authorization=Basic ${key}  Content-Type=application/x-www-form-urlencoded
    ${payload} =  Set Variable  username=${user}&password=${password}&grant_type=password 
    ${result} =  Invoke Service  POST  ${request_headers}  ${api_oauth_url}  ${payload}
    Should Be Equal As Integers  ${result.statusCode}   200   Failed to get access_token
    Save To Cache  ${user}  ${result.response["access_token"]}  ${result.response["expires_in"]}  300
    ${access_token} =  Set Variable  ${result.response["access_token"]}
    [Return]  ${access_token}
    
Get Request Headers
    [Documentation]    Get the required headers for invoke api. It will include the Authorization and Content-Type
    [Arguments]  ${user}   ${password}
    ${access_token} =  Get From Cache  ${user}
    ${access_token} =  Run Keyword If  '${access_token}' == 'None'  Get Access Token  ${user}  ${password}
    ...    ELSE   Set Variable  ${access_token}
    ${dictionary} =  Create Dictionary
    ...    Authorization=Bearer ${access_token}
    ...    Content-Type=application/json
    ${request_headers} =  Set Variable  ${dictionary}   
    Set Suite Variable  ${request_headers}
    [Return]  ${request_headers}