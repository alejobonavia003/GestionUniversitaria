package com.is1.proyecto.models;

import com.is1.proyecto.integration.IntegrationTestBase; // Importa la clase base
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de Integración para verificar las relaciones de los modelos académicos.
 * Hereda de IntegrationTestBase para manejar la conexión a la DB.
 */
public class EstructuraAcademicaTest extends IntegrationTestBase {

    @Test
    public void testCarreraHasManyPlanEstudio() {
        // ARRANGE (Preparar)
        // 1. Crear y guardar la Carrera (Padre)
        Carrera carrera = new Carrera();
        carrera.setNombre("Ingeniería en Software");
        carrera.setDuracion(5);
        carrera.saveIt(); // Guarda en la DB de prueba

        // 2. Crear el Plan de Estudio (Hijo)
        PlanEstudio plan2025 = new PlanEstudio();
        plan2025.setAnioPlan(2025);
        plan2025.setVersion(1);

        // ACT (Actuar)
        // 3. Asociar el hijo al padre usando .add()
        // Esto debería guardar automáticamente plan2025 y asignar la FK 'cod_carrera'
        carrera.add(plan2025);

        // ASSERT (Verificar)
        // 4. Refrescar la instancia de Carrera para cargar las relaciones
        carrera.refresh();
        
        // 5. Verificar que la Carrera ahora "tiene" el Plan
        List<PlanEstudio> planes = carrera.getAll(PlanEstudio.class);
        
        assertNotNull(planes, "La lista de planes no debe ser nula.");
        assertEquals(1, planes.size(), "Debe haber 1 plan de estudio asociado.");
        assertEquals(2025, planes.get(0).getAnioPlan(), "El año del plan debe ser 2025.");
    }

    @Test
    public void testPlanEstudioBelongsToCarrera() {
        // ARRANGE
        Carrera carrera = new Carrera();
        carrera.setNombre("Licenciatura en Computación");
        carrera.setDuracion(4);
        carrera.saveIt();

        PlanEstudio plan2023 = new PlanEstudio();
        plan2023.setAnioPlan(2023);
        carrera.add(plan2023); // Asocia y guarda

        // ACT
        // 1. Buscar el Plan de Estudio guardado
        // (Usamos findFirst para asegurar que lo traemos de la DB)
        PlanEstudio planGuardado = PlanEstudio.findFirst("anio_plan = ?", 2023);
        assertNotNull(planGuardado, "El plan de estudio debe encontrarse en la DB.");

        // 2. Navegar "hacia arriba" al padre
        Carrera carreraPadre = planGuardado.parent(Carrera.class);

        // ASSERT
        assertNotNull(carreraPadre, "El plan debe tener una carrera padre.");
        assertEquals("Licenciatura en Computación", carreraPadre.getNombre(), "El nombre de la carrera padre debe coincidir.");
    }
}