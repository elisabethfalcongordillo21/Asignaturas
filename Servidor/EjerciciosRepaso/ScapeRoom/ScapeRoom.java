package Servidor.EjerciciosRepaso.ScapeRoom;

import java.util.*;

import java.util.stream.Collectors;

public class ScapeRoom extends Juego {

    private String tematica;

    private List<Puzzle> puzzles;

    private int tiempoRestante;

    private String nivelDificultad;

    public ScapeRoom(String nombre, String tematica, int maxJugadores,

                     int duracionMinutos) {

        super(nombre, maxJugadores, duracionMinutos);

        this.tematica = tematica;

        this.puzzles = new ArrayList<>();

        this.tiempoRestante = duracionMinutos;

        this.nivelDificultad = "Media";

    }

    // Constructor con valores por defecto

    public ScapeRoom(String nombre, String tematica) {

        this(nombre, tematica, 6, 60);

    }

    public String getTematica() {

        return tematica;

    }

    public void setTematica(String tematica) {

        this.tematica = tematica;

    }

    // Encapsulación defensiva: se devuelve una copia para que quien reciba

    // la lista no pueda modificar la lista interna del objeto

    public List<Puzzle> getPuzzles() {

        return new ArrayList<>(puzzles);

    }

    public int getNumPuzzles() {

        return puzzles.size();

    }

    //Funcion de cuenta con filtro de funcion booleana

    public long getPuzzlesResueltos() {

        return puzzles.stream()

                      .filter(Puzzle::isResuelto)

                      //equivalente .filter(p -> p.isResuelto())

                      .count();

    }

    public String getNivelDificultad() {

        return nivelDificultad;

    }

    public void setNivelDificultad(String nivelDificultad) {

        this.nivelDificultad = nivelDificultad;

    }

    // añade un método getPuzzlesPendientes() 
    // que devuelva (como long) cuántos puzzles quedan sin resolver.

    public long getPuzzlesPendientes(){

        return  getNumPuzzles() - getPuzzlesResueltos();
       
    }

    public void agregarPuzzle(Puzzle puzzle) {

        if (puzzle != null) {

            puzzles.add(puzzle);

            System.out.println("Puzzle '" + puzzle.getNombre() +

                             "' agregado al escape room");

        } else {

            System.err.println("Error: No se puede agregar un puzzle nulo");

        }

    }

    // Varargs: permite pasar cero, uno o varios puzzles separados por comas

    public void agregarPuzzles(Puzzle... nuevosPuzzles) {

        for (Puzzle puzzle : nuevosPuzzles) {

            agregarPuzzle(puzzle);

        }

    }

    public boolean eliminarPuzzle(int puzzleId) {

        boolean eliminado = puzzles.removeIf(p -> p.getId() == puzzleId);

        if (eliminado) {

            System.out.println("Puzzle con ID " + puzzleId + " eliminado");

        } else {

            System.out.println("No se encontró puzzle con ID " + puzzleId);

        }

        return eliminado;

    }

    public Puzzle obtenerPuzzlePorIndice(int indice) {

        try {

            return puzzles.get(indice);

        } catch (IndexOutOfBoundsException e) {

            System.err.println("Error: No existe puzzle en la posición " + indice);

            return null;

        }

    }

    // Método auxiliar para mostrar todos los puzzles por consola

    public void listarPuzzles() {

        puzzles.forEach(System.out::println);

    }

    //  añade un método existePuzzle(int puzzleId)
    //  que devuelva true si hay algún puzzle con ese id.
    //  Utiliza anyMatch() sobre el stream de puzzles.

    public boolean existePuzzle(int puzzleId){
        
        return puzzles.stream().anyMatch(p->p.getId()==puzzleId);

    }

  public Puzzle buscarPuzzle(String nombre) {

        for (Puzzle puzzle : puzzles) {

            if (puzzle.getNombre().toLowerCase().contains(nombre.toLowerCase())) {

                return puzzle;

            }

        }

        return null;

    }

    public Optional<Puzzle> buscarPuzzleStream(String nombre) {

        return puzzles.stream()

                      .filter(p -> p.getNombre().toLowerCase()

                                   .contains(nombre.toLowerCase()))

                      .findFirst();

    }
// Comprobar si queda al menos un puzzle pendiente con intentos disponibles

//boolean continuar = escape.getPuzzles().stream()

   // .anyMatch(p -> !p.isResuelto());

// findFirst() y findAny() hacen short-circuit: paran en cuanto encuentran resultado

public static boolean todosSonResolubles(List<Puzzle> puzzles) {

    return puzzles.stream()

        .filter(p -> !p.isResuelto())

        .findAny()

        .isPresent();

}

// Ejemplo de map/reduce numérico: media del número de intentos

//double intentosMedios = escape.getPuzzles().stream()

    //.filter(Puzzle::isResuelto)

    //.mapToInt(Puzzle::getIntentos)

    //.average()

    //.orElse(0.0);


//}

//añade un método contarPuzzlesConNombre(String texto) que devuelva (como long) cuántos puzzles tienen texto en su nombre.

public long contarPuzzlesConNombre(String texto){
    return puzzles.stream().filter(p->p.getNombre().toLowerCase().contains(texto.toLowerCase())).count();
}
}