CREATE DATABASE Ventas;

\c Ventas

CREATE TABLE IF NOT EXISTS ventas (
  venta_id SERIAL PRIMARY KEY,
  vendedor_id INT,
  region TEXT,
  fecha DATE,
  importe NUMERIC(12,2)
);

INSERT INTO ventas (vendedor_id, region, fecha, importe) VALUES
-- ENERO 2026
(10, 'norte', '2026-01-05', 1200.00),
(11, 'norte', '2026-01-12', 850.50),
(13, 'norte', '2026-01-20', 2100.00),
(12, 'sur',   '2026-01-10', 400.00),
(14, 'sur',   '2026-01-15', 1500.00),
(15, 'centro','2026-01-05', 3000.00),
(16, 'centro','2026-01-22', 1200.00),

-- FEBRERO 2026
(10, 'norte', '2026-02-02', 3000.00),
(11, 'norte', '2026-02-14', 2800.00),
(13, 'norte', '2026-02-20', 1500.00),
(12, 'sur',   '2026-02-05', 2200.00),
(14, 'sur',   '2026-02-18', 2500.00),
(15, 'centro','2026-02-10', 900.00),
(16, 'centro','2026-02-25', 4500.00),

-- MARZO 2026
(10, 'norte', '2026-03-01', 1100.00),
(11, 'norte', '2026-03-10', 4200.00),
(13, 'norte', '2026-03-25', 900.00),
(12, 'sur',   '2026-03-05', 1800.00),
(14, 'sur',   '2026-03-15', 1850.00),
(15, 'centro','2026-03-02', 2500.00),
(16, 'centro','2026-03-20', 2500.00);

WITH ventas_mensuales AS (
    SELECT
        vendedor_id,
        region,
        EXTRACT(MONTH FROM fecha) as mes,
        SUM(ventas.importe) AS importe_mensual
    FROM ventas
    GROUP BY region, mes, vendedor_id
),
ranking_vendedores AS (
    SELECT

        RANK() OVER (
            PARTITION BY region, mes
            ORDER BY importe_mensual DESC
        ) AS pos_ranking,

        vendedor_id,
        region,
        mes,
        importe_mensual,


        ROUND(
            (importe_mensual / SUM(importe_mensual) over (partition by region, mes)) * 100,
            2
        ) as porcentaje_region

    FROM ventas_mensuales
)
SELECT * FROM ranking_vendedores
WHERE pos_ranking <= 3
ORDER BY region, mes, pos_ranking;

