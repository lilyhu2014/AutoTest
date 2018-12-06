*** Settings ***
Resource   ..${/}config${/}runtime.robot
Resource   ..${/}keywords${/}common.robot
Resource   ..${/}keywords${/}api.robot

Test Setup  Run Keywords   Use Admin Account   Connect TIANFU Database
Test Teardown  	Run Keyword    Disconnect TIANFU Database

*** Test Case ***
Create Composition
    ${api_url}=  Set Variable   ${BASE_API_URL}/compositions
    ${author}=  Set Variable  王维
    ${name}=   Set Variable   周末愉快
    ${current_date}=  Get Current Date  time_zone=UTC  exclude_millis=true   result_format=%Y-%m-%dT%H:%M:%SZ
    
    ${request_body}=  Catenate  $.copyright_owner REPLACE WITH "${author}";
    ...    $.name REPLACE WITH "${name}";
    ...    $.completed_at REPLACE WITH "${current_date}";
    ...    $.published_at REPLACE WITH "${current_date}";
    ...    $.detail["author"] REPLACE WITH "${author}";
    
    ${payload}=   Build Json Template  ${CURDIR}/createComposition.request   ${request_body}
    
    ${response}=  Invoke Service   POST  ${request_headers}   ${api_url}  ${payload}
    
    Should Be Equal As Integers   200   ${response.statusCode}
    
    ${expected_response_values}=  Catenate  $.status REPLACE WITH "SUCCESS";
    
    ${expected_response}=  Build Json Template  ${CURDIR}/createComposition.response  ${expected_response_values}
    
    Validate Json Object   ${expected_response}   ${response.response}
    
    Set Sql Resource   ${CURDIR}/createComposition.sql
    
    ${db_result}=   Get DB Values  TIANFU.get_composition_detail("${name}")
    
    ${db_result_length}=   Get Length   ${db_result}
    
    Should Be Equal As Integers   1   ${db_result_length}