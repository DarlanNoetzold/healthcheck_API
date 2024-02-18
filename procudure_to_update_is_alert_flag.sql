CREATE OR REPLACE PROCEDURE update_measurement_calculated_field()
LANGUAGE plpgsql AS $$
DECLARE
    metric_name VARCHAR;
    average DOUBLE PRECISION;
    stddev DOUBLE PRECISION;
    threshold DOUBLE PRECISION;
    cur CURSOR FOR SELECT DISTINCT name FROM metric_response;
BEGIN
    OPEN cur;
    LOOP
        FETCH cur INTO metric_name;
        EXIT WHEN NOT FOUND;

        -- Calcula a média e o desvio padrão dos values de todas as Measurements com o mesmo name
        SELECT AVG(m.value), STDDEV(m.value) INTO average, stddev
        FROM measurement m
        JOIN metric_response mr ON m.metric_response_id = mr.id
        WHERE mr.name = metric_name;

        -- Calcula o limiar como média + desvio padrão
        threshold := average + stddev;

        -- Atualiza is_alert para cada Measurement baseado no limiar calculado
        UPDATE measurement m
        SET is_alert = CASE WHEN m.value > threshold THEN TRUE ELSE FALSE END
        FROM metric_response mr
        WHERE m.metric_response_id = mr.id AND mr.name = metric_name;
    END LOOP;
    CLOSE cur;
END;
$$;

CALL update_measurement_calculated_field();