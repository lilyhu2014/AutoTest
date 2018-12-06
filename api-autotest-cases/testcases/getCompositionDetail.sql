TIANFU.get_composition_id = select id from t_composition order by created_by desc limit 1;
TIANFU.get_composition_detail = select * from t_composition where id = ?;