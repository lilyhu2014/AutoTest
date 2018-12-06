*** Settings ***
Resource   ..${/}robotkeywords${/}Common.robot

Test Setup  	Run Keywords  Connect Database
Test Teardown  	Run Keywords    Disconnect Database

*** Test Case ***
Get Connection
    Set Sql Resource   ${CURDIR}/test.sql
    ${composition_id}=  Get DB Value  TIANFU.get_composition.id
    ${res}=  Get DB Values  TIANFU.get_composition_detail(${composition_id})
    ${res_length}=  Get Length   ${res}
    ${res}=   Set Variable  ${res[0]}
    log  ${res}
    ${response_value}=  Catenate  $.id  REPLACE WITH  ${composition_id};
    ...    $.name  REPLACE WITH  "${res["name"]}";
    ...    $.copyright_owner  REPLACE WITH   "${res["copyright_owner"]}";
    ...    $.status  REPLACE WITH   "${res["status"]}";
    ...    $.type  REPLACE WITH   "${res["type"]}";
    ...    $.sold_out  REPLACE WITH   "${res["sold_out"]}";
    ...    $.sold_count  REPLACE WITH   "${res["sold_count"]}";
    ...    $.view_count  REPLACE WITH   "${res["view_count"]}";
    ...    $.composition_url  REPLACE WITH   "${res["composition_url"]}";
    ...    $.cover_url  REPLACE WITH   "${res["cover_url"]}";
    ...    $.desc  REPLACE WITH   "${res["desc"]}";
    ...    $.completed_site  REPLACE WITH   "${res["completed_site"]}";
    ...    $.recommend_min_price  REPLACE WITH  ${res["recommend_min_price"]}
    
    ${expected_response}=  Build Json Template  ${CURDIR}/test.response   ${response_value}
    log  ${expected_response}
