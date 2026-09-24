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


 public void ordenarPuzzlesPorPuntos() {

        puzzles.sort((p1, p2) -> Integer.compare(p2.getPuntos(), p1.getPuntos()));

        System.out.println("Puzzles ordenados por puntuación");

    }

    public List<Puzzle> filtrarPuzzlesPorPuntos(int minPuntos, int maxPuntos) {

        return puzzles.stream()

                      .filter(p -> p.getPuntos() >= minPuntos &&

                                  p.getPuntos() <= maxPuntos)

                      .collect(Collectors.toList());

    }

    public List<Puzzle> obtenerTopPuzzles(int n) {

        return puzzles.stream()

                      .sorted((p1, p2) -> Integer.compare(p2.getPuntos(),

                                                          p1.getPuntos()))

                      .limit(n)

                      .collect(Collectors.toList());

    }

    // añade un método obtenerPuzzlesMenosValiosos(int n) que devuelva los n puzzles de menor puntuación.

    public List<Puzzle> obtenerPuzzlesMenosValiosos(int n){
        return puzzles.stream()
                       .sorted((p1,p2)->Integer.compare(p1.getPuntos(),p2.getPuntos()))
                       .limit(n)
                       .collect(Collectors.toList());
    }
 
 
    @Override

    public void iniciarJuego() {

        super.iniciarJuego();  // Llamamos al método del padre

        this.tiempoRestante = getDuracionMinutos();

        puzzles.forEach(Puzzle::reiniciar);

        System.out.println("Escape Room '" + getNombre() + "' iniciado");

        System.out.println("Tienes " + tiempoRestante + " minutos para escapar");

        System.out.println("Resuelve " + getNumPuzzles() + " puzzles para completarlo");

    }

    public double calcularProgreso() {

        if (puzzles.isEmpty()) {

            return 0.0;

        }

        return (getPuzzlesResueltos() * 100.0) / getNumPuzzles();

    }

    public boolean estaCompletado() {

        return puzzles.stream().allMatch(Puzzle::isResuelto);

    }

    //añade un método hayPuzzlesPendientes() que devuelva true si queda al menos un puzzle sin resolver 
    // y hayNPuzzlesPendientes(int n) que devuelva true si quedan n puzzles sin resolver. 

    public boolean hayPuzzlesPendientes(){
        return puzzles.stream().anyMatch(p->!p.isResuelto());
    }

    public boolean hayNPuzzlesPendientes(int n){
        return puzzles.stream()
        .filter(p->!p.isResuelto())
        .count()==n;
    }


     public Map<String, Object> obtenerEstadisticas() {

        Map<String, Object> stats = new HashMap<>();

        stats.put("nombre", getNombre());

        stats.put("tematica", tematica);

        stats.put("totalPuzzles", getNumPuzzles());

        stats.put("puzzlesResueltos", getPuzzlesResueltos());

        stats.put("puzzlesPendientes", getNumPuzzles() - getPuzzlesResueltos());

        stats.put("progreso", String.format("%.1f%%", calcularProgreso()));

        stats.put("puntuacionTotal", getPuntuacion());

        stats.put("puntosPosibles", puzzles.stream()

                                          .mapToInt(Puzzle::getPuntos)

                                          .sum());

        stats.put("intentosTotales", puzzles.stream()

                                           .mapToInt(Puzzle::getIntentos)

                                           .sum());

        return stats;

    }

    public void intercambiarPuzzles(int indice1, int indice2) {

        try {

            Collections.swap(puzzles, indice1, indice2);

            System.out.println("Puzzles intercambiados exitosamente");

        } catch (IndexOutOfBoundsException e) {

            System.err.println("Error: Índices fuera de rango");

        }

    }

    public void barajarPuzzles() {

        Collections.shuffle(puzzles);

        System.out.println("Puzzles barajados aleatoriamente");

    }

    @Override

    public String toString() {

        double progreso = calcularProgreso();

        return String.format(

            "Escape Room: %s\n" +

            "   Temática: %s\n" +

            "   Puzzles: %d/%d\n" +

            "   Progreso: %.1f%%\n" +

            "   Puntuación: %d",

            getNombre(), tematica, getPuzzlesResueltos(),

            getNumPuzzles(), progreso, getPuntuacion()

        );

    }

    //añade un método moverPuzzleAlPrincipio(int indice) que intercambie el puzzle de esa posición 
    // con el que está en la posición 0, usando Collections.swap()controlando también con try/catch 
    // un índice fuera de rango.

    public void moverPuzzleAlPrincipio(int indice){

         try {

            Collections.swap(puzzles, 0, indice);

            System.out.println("Puzzles intercambiados exitosamente");

        } catch (IndexOutOfBoundsException e) {

            System.err.println("Error: Índices fuera de rango");

        }
    }


}


