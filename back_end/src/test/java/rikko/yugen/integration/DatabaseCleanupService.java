package rikko.yugen.integration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DatabaseCleanupService {
    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;

    @Transactional
    public void execute() {
        if (tableNames == null) {
            tableNames = entityManager.getMetamodel().getEntities().stream()
                    .map(entityType -> {
                        Table table = entityType.getJavaType().getAnnotation(Table.class);
                        if (table != null) {
                            return table.name();
                        } else {
                            return entityType.getName().toLowerCase();
                        }
                    })
                    .toList();
        }

        entityManager.flush();
        entityManager.createNativeQuery("SET CONSTRAINTS ALL DEFERRED").executeUpdate();

        for (String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName + " RESTART IDENTITY CASCADE").executeUpdate();
        }
    }
}