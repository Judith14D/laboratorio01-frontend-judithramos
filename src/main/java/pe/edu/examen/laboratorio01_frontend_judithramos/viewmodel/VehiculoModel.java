package pe.edu.examen.laboratorio01_frontend_judithramos.viewmodel;

import pe.edu.examen.laboratorio01_frontend_judithramos.dto.VehiculosResponseDTO;

public record VehiculoModel(String codigo, String mensaje, String marca, String modelo, int nroAsientos, double precio, String color)  {
}
