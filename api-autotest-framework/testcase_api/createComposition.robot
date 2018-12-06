*** Settings ***
Resource   ..${/}robotkeywords${/}Common.robot

Test Setup  	Run Keywords    Connect Database
Test Teardown  	Run Keywords    Disconnect Database

*** Test Case ***
Create Composition
    Set Sql Resource   ${CURDIR}/createComposition.sql
    ${api_url}=  Set Variable   ${base_api_url}/compositions
    ${author}=  Set Variable   Barbara Perez
    ${name}=  Set Variable   Hbnqu Jfceim Soempm Ltnkrvhe
    # # create post body
    # ${request_template_values}=  Catenate  $.copyright_owner REPLACE WITH "${author}";
    # ...   $.name REPLACE WITH "${name}";
    # ...   $.detail["author"] REPLACE WITH "${author}";
    
    # ${payload}=  Build Json Template  ${CURDIR}/createComposition.request  ${request_template_values}
    
    # ${result}=  Invoke Service  POST  ${request_headers}  ${api_url}  ${payload}
    
    # Should Be Equal As Integers   200    ${result.statusCode}
    
    # ${expected_response_values}=  Catenate  $.status REPLACE WITH "SUCCESS";
    
    # ${expected_response}=  Build Json Template  ${CURDIR}/createComposition.response  ${expected_response_values}
    # Validate Json Object  ${expected_response}  ${result.response}
    
    ${db_result}=  Get DB Values  TIANFU.get_composition_detail("${name}")
    log  ${db_result}
    ${db_result_length}=  Get Length  ${db_result}
    
    Should Be Equal As Integers  1  ${db_result_length}
