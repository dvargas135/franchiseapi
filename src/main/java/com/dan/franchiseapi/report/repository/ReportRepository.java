package com.dan.franchiseapi.report.repository;

import com.dan.franchiseapi.report.dto.TopStockProductResponse;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class ReportRepository {

    private final DatabaseClient databaseClient;

    public ReportRepository(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    public Flux<TopStockProductResponse> findTopStockProductsByFranchiseId(Long franchiseId) {
        String sql = """
                SELECT 
                    b.id AS branch_id,
                    b.name AS branch_name,
                    p.id AS product_id,
                    p.name AS product_name,
                    p.stock AS stock
                FROM branches b
                JOIN products p ON p.branch_id = b.id
                JOIN (
                    SELECT 
                        branch_id,
                        MAX(stock) AS max_stock
                    FROM products
                    GROUP BY branch_id
                ) max_products 
                    ON max_products.branch_id = p.branch_id
                   AND max_products.max_stock = p.stock
                WHERE b.franchise_id = :franchiseId
                ORDER BY b.id, p.id
                """;

        return databaseClient.sql(sql)
                .bind("franchiseId", franchiseId)
                .map((row, metadata) -> new TopStockProductResponse(
                        row.get("branch_id", Long.class),
                        row.get("branch_name", String.class),
                        row.get("product_id", Long.class),
                        row.get("product_name", String.class),
                        row.get("stock", Integer.class)
                ))
                .all();
    }

}
