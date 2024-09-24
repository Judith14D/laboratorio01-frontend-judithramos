package pe.edu.examen.laboratorio01_frontend_judithramos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import pe.edu.examen.laboratorio01_frontend_judithramos.dto.VehiculosRequestDTO;
import pe.edu.examen.laboratorio01_frontend_judithramos.dto.VehiculosResponseDTO;
import pe.edu.examen.laboratorio01_frontend_judithramos.viewmodel.VehiculoModel;

@Controller
@RequestMapping("/buscarVehiculo")
public class VehiculosController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/index")
    public String mostrarForm(Model modelo) {
        VehiculoModel vehiculo = new VehiculoModel("00", "", "", "", 0, 0.0, "");
        modelo.addAttribute("vehiculoModel", vehiculo);
        return "index";
    }

    @PostMapping("/busqueda")
    public String buscarVehiculo(@RequestParam("placa") String placa, Model model) {
        if (placa == null || placa.trim().isEmpty() || placa.length() != 8 || !placa.matches("^[A-Za-z]{3}-\\d{4}$")) {
            VehiculoModel vehiculoModel = new VehiculoModel("01", "Debe ingresar una placa correcta", "", "", 0, 0.0, "");
            model.addAttribute("vehiculoModel", vehiculoModel);
            return "index";
        }


        try {
            String endpoint = "http://localhost:8081/vehiculos/busqueda";
            VehiculosRequestDTO vehiculoRequest = new VehiculosRequestDTO(placa);
            VehiculosResponseDTO vehiculoResponse = restTemplate.postForObject(endpoint, vehiculoRequest, VehiculosResponseDTO.class);


            if (vehiculoResponse != null) {
                if (vehiculoResponse.codigo().equals("00")) {
                    VehiculoModel vehiculoModel = new VehiculoModel("00", "",
                            vehiculoResponse.marca(),
                            vehiculoResponse.modelo(),
                            vehiculoResponse.nroAsientos(),
                            vehiculoResponse.precio(),
                            vehiculoResponse.color());
                    model.addAttribute("vehiculoModel", vehiculoModel);
                    return "informacion";
                } else {
                    VehiculoModel vehiculoModel = new VehiculoModel(
                            "01",
                            "No se encontró un vehículo para la placa ingresada",
                            "", "", 0, 0.0, "");
                    model.addAttribute("vehiculoModel", vehiculoModel);
                    return "index";
                }
            } else {
                VehiculoModel vehiculoModel = new VehiculoModel(
                        "99",
                        "Error: Ocurrió un problema con la respuesta del servidor",
                        "", "", 0, 0.0, "");
                model.addAttribute("vehiculoModel", vehiculoModel);
                return "index";
            }
        } catch (Exception e) {
            VehiculoModel vehiculoModel = new VehiculoModel(
                    "99",
                    "Error: Ocurrió un problema inesperado",
                    "", "", 0, 0.0,"");
            model.addAttribute("vehiculoModel", vehiculoModel);
            System.out.println(e.getMessage());
            return "index";
        }
    }

}
