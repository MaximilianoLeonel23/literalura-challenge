package com.example.literalura.principal;

import com.example.literalura.model.Autor;
import com.example.literalura.model.DatosAPI;
import com.example.literalura.model.Libro;
import com.example.literalura.model.LibroRecord;
import com.example.literalura.repository.AutorRepository;
import com.example.literalura.repository.LibrosRepository;
import com.example.literalura.service.ConsumoAPI;
import com.example.literalura.service.ConvertirDatos;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner sc = new Scanner(System.in);
    private String BASE_URL = "https://gutendex.com/books";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvertirDatos convertirDatos = new ConvertirDatos();
    private LibrosRepository librosRepository;
    private AutorRepository autorRepository;

    public Principal(LibrosRepository librosRepository, AutorRepository autorRepository) {
        this.librosRepository = librosRepository;
        this.autorRepository = autorRepository;
    }

    public void mostrarMenu() {
        int opc = -1;
        do {
            System.out.println("""
                    --------------
                    Elija la opción a través de su número:
                    1. buscar libro por título
                    2. listar libros registrados
                    3. listar autores registrados
                    4. listar autores vivos en un determinado año
                    5. listar libros por idioma
                    0. salir
                    """);
            opc = sc.nextInt();
            sc.nextLine();


            switch (opc) {
                case 1:
                    obtenerLibro();
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresPorAnio();
                    break;
                case 5:
                    listarAutoresPorIdioma();
                    break;
                case 0:
                    System.out.println("Saliendo de la aplicación");
                    break;
                default:
                    System.out.println("Ingrese una opción válida");
            }

        } while (opc != 0);
    }


    private void obtenerLibro() {
        System.out.println("Ingrese el nombre del libro que desea buscar: ");
        var titulo = sc.nextLine();
        String url = BASE_URL + "/?search=" + titulo.replace(" ", "+");
        var json = consumoAPI.obtenerDatos(url);
        DatosAPI datos = convertirDatos.obtenerDatos(json, DatosAPI.class);

        Optional<LibroRecord> libroBuscado = datos.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(titulo.toUpperCase()))
                .findFirst();


        if (libroBuscado.isPresent()) {
            Optional<Libro> libroExistente = librosRepository.findByTitulo(libroBuscado.get().titulo());
            if (libroExistente.isPresent()) {
                System.out.println("El libro ya está registrado");
            } else {

                System.out.println("Libro encontrado");
                Libro libro = new Libro(libroBuscado.get());
                librosRepository.save(libro);
            }
        } else {
            System.out.println("Libro no encontrado");
        }
    }

    private void listarLibros() {
        List<Libro> libros = librosRepository.findAll();
        libros.forEach(e -> {
            String autorNombre = "Unknown";
            if (!e.getAutores().isEmpty()) {
                autorNombre = e.getAutores().get(0).getNombre();
            }
            System.out.println("Título: " + e.getTitulo() + " (" + autorNombre + ")");
        });
    }

    private void listarAutores() {
        List<Autor> autores = autorRepository.findAll();
        autores.stream().forEach(a -> System.out.println("Autor: " + a.getNombre() + " - Fecha de nacimiento: " + a.getFechaDeNacimiento() + " - Fecha de fallecimiento: " + a.getFechaDeFallecimiento()));
    }

    private void listarAutoresPorAnio() {
        System.out.println("¿En qué año quiere buscar?");
        int anio = sc.nextInt();
        sc.nextLine();
        List<Autor> autores = autorRepository.findByFechaDeNacimientoLessThanAndFechaDeFallecimientoGreaterThan(anio, anio);
        autores.stream().forEach(a -> System.out.println("Autor: " + a.getNombre() + " - Fecha de nacimiento: " + a.getFechaDeNacimiento() + " - Fecha de fallecimiento: " + a.getFechaDeFallecimiento()));
    }

    private void listarAutoresPorIdioma() {
        System.out.println("¿Qué idioma está buscando? Elija: en - es - fr");
        String idioma = sc.nextLine();
        List<Libro> libros = librosRepository.findAll();
        List<Libro> librosPorIdioma = libros.stream()
                .filter(l -> l.getIdiomas().contains(idioma))
                .toList();
        if (!librosPorIdioma.isEmpty()) {
            librosPorIdioma.stream().forEach(l -> System.out.println(l.getTitulo()));
        } else {
            System.out.println("Libros no encontrados");
        }

    }

}
