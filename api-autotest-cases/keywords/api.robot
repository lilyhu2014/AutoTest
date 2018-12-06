*** Keywords ***
Use Admin Account
    Get Request Headers  ${ADMIN_USER}  ${ADMIN_PASSWORD}
    
Get Access Token
    [Documentation]    Get the oauth token and save to cache.
    [Arguments]   ${user}    ${password}
    ${key} =  Encode Base64  ${OAUTH_USER}:${OAUTH_PASSWORD}
    ${request_headers} =  Create Dictionary  Authorization=Basic ${key}  Content-Type=application/x-www-form-urlencoded
    ${payload} =  Set Variable  username=${user}&password=${password}&grant_type=password 
    ${result} =  Invoke Service  POST  ${request_headers}  ${OAUTH_API_URL}  ${payload}
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
    
Connect Database
    [Documentation]    establish the database connection
    [Arguments]   ${dbName}    ${url}    ${dbUser}    ${dbPassword}
    Connect To Database   ${dbName}   ${DB_DRIVER}  ${url}    ${dbUser}    ${dbPassword}
    
Disconnect Database
    [Documentation]    Close the database connection
    [Arguments]    ${dbName}
    Disconnect From Database   ${dbName}
    
Connect TIANFU Database
    [Documentation]    Connect to TIANFU database
    Connect Database  TIANFU   ${DB_URL}  ${DB_USER}   ${DB_PASSWORD}
    
Disconnect TIANFU Database
    [Documentation]    Release the TIANFU database connection
    Disconnect Database  TIANFU
    
    