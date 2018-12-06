*** Settings ***
Resource   ..${/}config${/}runtime.robot
Resource   ..${/}keywords${/}common.robot
Resource   ..${/}keywords${/}api.robot

Test Setup  Run Keywords   Use Admin Account   Connect TIANFU Database
Test Teardown  	Run Keyword    Disconnect TIANFU Database

Test Template   Get Composition Details

*** Test Cases ***                   expected_status_code       composition_id_type       error_msg
Get an existing composition                 200                  valid_id                 ${empty}
Get an non-existing composition             404                  invalid_id               Composition not found by id: ${INVALID_ID}
Get an composition with non-numberic        400                  non_numberic_id          Failed to convert request
    
*** Keywords ***
Get Composition Details
    [Arguments]   ${expected_status_code}    ${composition_id_type}      ${error_msg}
    Set Sql Resource   ${CURDIR}/getcompositionDetail.sql
    ${composition_id}=  Initialize Params   ${composition_id_type}
    ${call_api_url} =  Set Variable  ${BASE_API_URL}/compositions/${composition_id}
    ${result} =  Invoke Service  GET  ${request_headers}  ${call_api_url}
    Should Be Equal As Integers   ${result.statusCode}    ${expected_status_code}
    Run Keyword If  ${expected_status_code} == 200   Validate 200 Response Body  ${composition_id}  ${result.response}
    ...    ELSE   Should Be Equal As Strings    ${error_msg}      ${result.response["message"]}     
 
Initialize Params
    [Arguments]    ${composition_id_type}
    ${composition_id}=   Run Keyword If  "${composition_id_type}" == "invalid_id"  Set Variable  ${INVALID_ID}
    ...    ELSE IF  "${composition_id_type}" == "non_numberic_id"  Set Variable   ${NON_NUMBERIC_ID}
    ...    ELSE   Get DB Value  TIANFU.get_composition_id.id   
    [Return]   ${composition_id}
    
Validate 200 Response Body
    [Arguments]  ${composition_id}   ${response}
    ${db_result}=  Get DB Values   TIANFU.get_composition_detail(${composition_id})
    ${db_result_length}=  Get Length   ${db_result}
    Should Be Equal As Integers   1   ${db_result_length}
    
    ${db_result}=  Set Variable  ${db_result[0]}
    ${expected_response_template} =  Catenate  $.id REPLACE WITH ${db_result["id"]};
    ...    $.name REPLACE WITH "${db_result["name"]}";
    ...    $.copyright_owner REPLACE WITH "${db_result["copyright_owner"]}";
    ...    $.status REPLACE WITH "${db_result["status"]}";
    ...    $.completed_site REPLACE WITH "${db_result["completed_site"]}"
    
    ${expected_response} =  Build Json Template   ${CURDIR}/getCompositionDetail.response  ${expected_response_template}
    Validate Json Object  ${expected_response}   ${response}
