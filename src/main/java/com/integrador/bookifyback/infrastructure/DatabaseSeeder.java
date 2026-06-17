package com.integrador.bookifyback.infrastructure;

import com.integrador.bookifyback.domain.autor.Autor;
import com.integrador.bookifyback.domain.autor.AutorRepository;
import com.integrador.bookifyback.domain.categoria.Categoria;
import com.integrador.bookifyback.domain.categoria.CategoriaRepository;
import com.integrador.bookifyback.domain.compra.Compra;
import com.integrador.bookifyback.domain.compra.CompraRepository;
import com.integrador.bookifyback.domain.libro.Libro;
import com.integrador.bookifyback.domain.libro.LibroRepository;
import com.integrador.bookifyback.domain.rol.Rol;
import com.integrador.bookifyback.domain.rol.RolRepository;
import com.integrador.bookifyback.domain.usuario.Usuario;
import com.integrador.bookifyback.domain.usuario.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Clase encargada de poblar la base de datos con datos de prueba al iniciar la
 * aplicación.
 * Solo se ejecuta si la base de datos está vacía (sin categorías registradas).
 */
@Component
public class DatabaseSeeder implements CommandLineRunner {

        private static final Logger log = LoggerFactory.getLogger(DatabaseSeeder.class);

        private final CategoriaRepository categoriaRepository;
        private final AutorRepository autorRepository;
        private final LibroRepository libroRepository;
        private final UsuarioRepository usuarioRepository;
        private final RolRepository rolRepository;
        private final CompraRepository compraRepository;
        private final PasswordEncoder passwordEncoder;

        public DatabaseSeeder(
                        CategoriaRepository categoriaRepository,
                        AutorRepository autorRepository,
                        LibroRepository libroRepository,
                        UsuarioRepository usuarioRepository,
                        RolRepository rolRepository,
                        CompraRepository compraRepository,
                        PasswordEncoder passwordEncoder) {
                this.categoriaRepository = categoriaRepository;
                this.autorRepository = autorRepository;
                this.libroRepository = libroRepository;
                this.usuarioRepository = usuarioRepository;
                this.rolRepository = rolRepository;
                this.compraRepository = compraRepository;
                this.passwordEncoder = passwordEncoder;
        }

        @Override
        @Transactional
        public void run(String... args) throws Exception {
                // Condicion de seguridad: si ya hay datos, no hacemos nada
                if (categoriaRepository.count() > 0) {
                        log.info("Base de datos ya contiene datos. Seeder omitido.");
                        return;
                }

                log.info("Iniciando siembra de datos de prueba...");

                // ====================================================
                // 1. ROLES (Los buscamos porque Flyway V2 ya los inserta)
                // ====================================================
                Rol rolAdmin = rolRepository.findByNombre("ROLE_ADMIN")
                                .orElseGet(() -> rolRepository.save(Rol.builder().nombre("ROLE_ADMIN").build()));
                Rol rolCliente = rolRepository.findByNombre("ROLE_CLIENTE")
                                .orElseGet(() -> rolRepository.save(Rol.builder().nombre("ROLE_CLIENTE").build()));
                Rol rolFactor = rolRepository.findByNombre("FACTOR_PASSWORD")
                                .orElseGet(() -> rolRepository.save(Rol.builder().nombre("FACTOR_PASSWORD").build()));

                // ====================================================
                // 2. USUARIOS DE PRUEBA
                // ====================================================
                String claveEncriptada = passwordEncoder.encode("Admin123!");

                Usuario admin = Usuario.builder()
                                .nombre("Administrador Bookify")
                                .correo("admin@bookify.com")
                                .clave(claveEncriptada)
                                .build();
                admin.getRoles().add(rolAdmin);
                admin.getRoles().add(rolFactor);
                usuarioRepository.save(admin);

                // Usuarios clientes para simular compras
                Usuario cliente1 = Usuario.builder()
                                .nombre("María García")
                                .correo("maria@bookify.com")
                                .clave(passwordEncoder.encode("Cliente123!"))
                                .build();
                cliente1.getRoles().add(rolCliente);
                cliente1.getRoles().add(rolFactor);
                usuarioRepository.save(cliente1);

                Usuario cliente2 = Usuario.builder()
                                .nombre("Carlos Rodríguez")
                                .correo("carlos@bookify.com")
                                .clave(passwordEncoder.encode("Cliente123!"))
                                .build();
                cliente2.getRoles().add(rolCliente);
                cliente2.getRoles().add(rolFactor);
                usuarioRepository.save(cliente2);

                Usuario cliente3 = Usuario.builder()
                                .nombre("Ana Martínez")
                                .correo("ana@bookify.com")
                                .clave(passwordEncoder.encode("Cliente123!"))
                                .build();
                cliente3.getRoles().add(rolCliente);
                cliente3.getRoles().add(rolFactor);
                usuarioRepository.save(cliente3);

                // ====================================================
                // 3. CATEGORIAS
                // ====================================================
                Categoria fantasia = categoriaRepository.save(Categoria.builder().nombre("Fantasía").build());
                Categoria cienciaFiccion = categoriaRepository
                                .save(Categoria.builder().nombre("Ciencia Ficción").build());
                Categoria programacion = categoriaRepository.save(Categoria.builder().nombre("Programación").build());
                Categoria desarrolloPersonal = categoriaRepository
                                .save(Categoria.builder().nombre("Desarrollo Personal").build());
                Categoria clasicos = categoriaRepository.save(Categoria.builder().nombre("Clásicos").build());
                Categoria thriller = categoriaRepository.save(Categoria.builder().nombre("Thriller").build());

                // ====================================================
                // 4. AUTORES
                // ====================================================
                Autor tolkien = autorRepository.save(Autor.builder().nombre("J.R.R. Tolkien").build());
                Autor rowling = autorRepository.save(Autor.builder().nombre("J.K. Rowling").build());
                Autor martin = autorRepository.save(Autor.builder().nombre("Robert C. Martin").build());
                Autor clear = autorRepository.save(Autor.builder().nombre("James Clear").build());
                Autor orwell = autorRepository.save(Autor.builder().nombre("George Orwell").build());
                Autor brown = autorRepository.save(Autor.builder().nombre("Dan Brown").build());
                Autor rothfuss = autorRepository.save(Autor.builder().nombre("Patrick Rothfuss").build());
                Autor sanderson = autorRepository.save(Autor.builder().nombre("Brandon Sanderson").build());
                Autor brooks = autorRepository.save(Autor.builder().nombre("Frederick P. Brooks Jr.").build());
                Autor kiyosaki = autorRepository.save(Autor.builder().nombre("Robert T. Kiyosaki").build());
                Autor asimov = autorRepository.save(Autor.builder().nombre("Isaac Asimov").build());
                Autor herbert = autorRepository.save(Autor.builder().nombre("Frank Herbert").build());
                Autor king = autorRepository.save(Autor.builder().nombre("Stephen King").build());
                Autor christie = autorRepository.save(Autor.builder().nombre("Agatha Christie").build());
                Autor lewis = autorRepository.save(Autor.builder().nombre("C.S. Lewis").build());
                Autor gaiman = autorRepository.save(Autor.builder().nombre("Neil Gaiman").build());
                Autor adams = autorRepository.save(Autor.builder().nombre("Douglas Adams").build());
                Autor harari = autorRepository.save(Autor.builder().nombre("Yuval Noah Harari").build());
                Autor carnegie = autorRepository.save(Autor.builder().nombre("Dale Carnegie").build());
                Autor marquez = autorRepository.save(Autor.builder().nombre("Gabriel García Márquez").build());

                // 5. LIBROS (con portadas reales de Open Library / covers.openlibrary.org)
                // ====================================================

                // --- Fantasía ---
                Libro elSenorDeLosAnillos = crearLibro(
                                "El Señor de los Anillos: La Comunidad del Anillo",
                                "La historia épica del hobbit Frodo Bolsón, quien hereda el Anillo Único, " +
                                                "una joya de poder maligno forjada por el Señor Oscuro Sauron. Frodo y su "
                                                +
                                                "fiel compañero Samwise emprenden un viaje que llevará a la Comunidad del Anillo "
                                                +
                                                "por tierras peligrosas y maravillosas en su misión de destruir el artefacto.",
                                new BigDecimal("55.00"), "EPUB",
                                "https://covers.openlibrary.org/b/id/13344549-L.jpg",
                                tolkien, fantasia);

                Libro elHobbit = crearLibro(
                                "El Hobbit",
                                "Bilbo Bolsón lleva una vida tranquila hasta que el mago Gandalf y trece " +
                                                "enanos lo arrastran a una aventura para recuperar el tesoro custodiado por "
                                                +
                                                "el temible dragón Smaug. Una historia de descubrimiento, valor y amistad que "
                                                +
                                                "se convirtió en una de las obras de fantasía más queridas de todos los tiempos.",
                                new BigDecimal("45.00"), "PDF",
                                "https://covers.openlibrary.org/b/id/15146094-M.jpg",
                                tolkien, fantasia);

                Libro harryPotter = crearLibro(
                                "Harry Potter y la Piedra Filosofal",
                                "Harry Potter descubre en su undécimo cumpleaños que es un mago y que el mundo " +
                                                "de los no magos, o «muggles», no es el único que existe. Ingresa al Colegio "
                                                +
                                                "Hogwarts de Magia y Hechicería y descubre sus orígenes y el destino que lo "
                                                +
                                                "espera como «el niño que sobrevivió».",
                                new BigDecimal("50.00"), "EPUB",
                                "https://covers.openlibrary.org/b/id/14925450-M.jpg",
                                rowling, fantasia);

                Libro nombreDelViento = crearLibro(
                                "El Nombre del Viento",
                                "La historia de Kvothe, un músico, mago y leyenda viviente que narra su propia " +
                                                "vida extraordinaria desde sus humildes orígenes hasta sus días en la Universidad "
                                                +
                                                "de Artes mágicas. Una épica crónica de amor, magia y tragedia.",
                                new BigDecimal("60.00"), "EPUB",
                                "https://covers.openlibrary.org/b/id/15154551-M.jpg",
                                rothfuss, fantasia);

                Libro elCaminoDeLosReyes = crearLibro(
                                "El Camino de los Reyes",
                                "Primer libro del ciclo de El Archivo de las Tormentas. En el mundo de Roshar, " +
                                                "azotado por poderosas tormentas mágicas, tres personas cuyas vidas están "
                                                +
                                                "entrelazadas deben enfrentarse a un conflicto que puede destruir la civilización.",
                                new BigDecimal("65.00"), "PDF",
                                "https://covers.openlibrary.org/b/id/13222477-M.jpg",
                                sanderson, fantasia);

                // --- Ciencia Ficción ---
                Libro milNovecientosOchentaYCuatro = crearLibro(
                                "1984",
                                "En un futuro totalitario, Winston Smith trabaja para el Ministerio de la Verdad " +
                                                "reescribiendo la historia. Su rebelión silenciosa contra el Gran Hermano y su "
                                                +
                                                "partido omnipotente lo lleva a descubrir el amor y la resistencia, con "
                                                +
                                                "consecuencias devastadoras. Una obra maestra de la distopía política.",
                                new BigDecimal("42.00"), "EPUB",
                                "https://covers.openlibrary.org/b/id/15158861-M.jpg",
                                orwell, cienciaFiccion);

                // --- Programación ---
                Libro cleanCode = crearLibro(
                                "Clean Code: A Handbook of Agile Software Craftsmanship",
                                "Un manual definitivo para escribir código limpio, mantenible y profesional. " +
                                                "Robert C. Martin comparte sus mejores prácticas y principios de la artesanía "
                                                +
                                                "del software, con ejemplos reales de código bueno y malo. Lectura obligatoria "
                                                +
                                                "para todo desarrollador que se tome en serio su profesión.",
                                new BigDecimal("70.00"), "PDF",
                                "https://covers.openlibrary.org/b/id/7236652-M.jpg",
                                martin, programacion);

                Libro elMiticoHombresMes = crearLibro(
                                "El Mítico Hombre-Mes",
                                "Ensayos sobre ingeniería de software por Frederick Brooks. Escrito en 1975 y " +
                                                "revisado en 1995, este libro sigue siendo extraordinariamente relevante. Brooks "
                                                +
                                                "aborda los problemas de la gestión de proyectos de software complejos con "
                                                +
                                                "perspicacia y humor, incluyendo su famosa ley: agregar personas a un proyecto "
                                                +
                                                "retrasado solo lo retrasa más.",
                                new BigDecimal("55.00"), "PDF",
                                "https://upload.wikimedia.org/wikipedia/en/f/fd/Mythical_man-month_%28book_cover%29.jpg",
                                brooks, programacion);

                // --- Desarrollo Personal ---
                Libro habitosAtomicos = crearLibro(
                                "Hábitos Atómicos",
                                "James Clear, uno de los expertos en hábitos más reconocidos del mundo, presenta " +
                                                "un marco probado para construir buenos hábitos y eliminar los malos. Este libro "
                                                +
                                                "te enseñará a hacer pequeños cambios del 1% que producen resultados notables a "
                                                +
                                                "largo plazo, revolucionando la manera en que pensamos sobre el progreso y el éxito.",
                                new BigDecimal("48.00"), "EPUB",
                                "https://covers.openlibrary.org/b/id/15125764-M.jpg",
                                clear, desarrolloPersonal);

                Libro padrePadreRico = crearLibro(
                                "Padre Rico, Padre Pobre",
                                "Robert Kiyosaki desafía la creencia convencional de que debes tener un gran " +
                                                "ingreso para ser rico. Relata las lecciones financieras que aprendió de sus "
                                                +
                                                "dos padres: su padre biológico (padre pobre) y el padre de su mejor amigo "
                                                +
                                                "(padre rico). Un clásico de la educación financiera personal.",
                                new BigDecimal("40.00"), "PDF",
                                "https://covers.openlibrary.org/b/id/15217276-M.jpg",
                                kiyosaki, desarrolloPersonal);

                // --- Thriller ---
                Libro elCodigoDaVinci = crearLibro(
                                "El Código Da Vinci",
                                "Robert Langdon, profesor de simbología de Harvard, es llamado al Museo del " +
                                                "Louvre de París, donde el curator del museo ha sido asesinado. Junto a la "
                                                +
                                                "criptóloga Sophie Neveu, deberá descifrar una serie de pistas ocultas en las "
                                                +
                                                "pinturas de Leonardo da Vinci que conducen a un secreto que podría sacudir "
                                                +
                                                "los cimientos del mundo occidental.",
                                new BigDecimal("46.00"), "EPUB",
                                "https://covers.openlibrary.org/b/id/15171890-M.jpg",
                                brown, thriller);

                Libro angelesYDemonios = crearLibro(
                                "Ángeles y Demonios",
                                "Una antigua hermandad secreta, los Illuminati, ha robado antimateria del CERN. Robert Langdon debe encontrar los escondites en el Vaticano antes de que explote.",
                                new BigDecimal("44.00"), "PDF",
                                "https://covers.openlibrary.org/b/id/12857022-M.jpg",
                                brown, thriller);

                Libro elResplandor = crearLibro(
                                "El Resplandor",
                                "Jack Torrance se convierte en el cuidador de invierno del hotel Overlook. A medida que el crudo invierno aísla el hotel, fuerzas malévolas comienzan a influir en su mente.",
                                new BigDecimal("50.00"), "EPUB",
                                "https://covers.openlibrary.org/b/id/14655766-M.jpg",
                                king, thriller);

                Libro it = crearLibro(
                                "It (Eso)",
                                "Siete niños de Derry, Maine, se enfrentan a un antiguo mal que cambia de forma y se alimenta del miedo, apareciendo a menudo como el payaso Pennywise.",
                                new BigDecimal("60.00"), "PDF",
                                "https://covers.openlibrary.org/b/id/14655624-M.jpg",
                                king, thriller);

                Libro diezNegritos = crearLibro(
                                "Y No Quedó Ninguno (Diez Negritos)",
                                "Diez extraños son invitados a una isla remota y comienzan a ser asesinados uno por uno según la letra de una vieja canción infantil.",
                                new BigDecimal("35.00"), "EPUB",
                                "https://images.cdn1.buscalibre.com/fit-in/360x360/58/3b/583b512451a75dff24c6e2dfcffa705c.jpg",
                                christie, thriller);

                // --- Mas Fantasía ---
                Libro narnia = crearLibro(
                                "El León, la Bruja y el Armario",
                                "Cuatro hermanos descubren un armario mágico que los transporta a Narnia, un mundo congelado en un invierno eterno por la Bruja Blanca.",
                                new BigDecimal("40.00"), "PDF",
                                "https://images.cdn2.buscalibre.com/fit-in/360x360/69/e8/69e8321769b9bc45f93d615d00cb54be.jpg",
                                lewis, fantasia);

                Libro americanGods = crearLibro(
                                "American Gods",
                                "Sombra es liberado de prisión y se ve envuelto en una guerra entre los dioses antiguos de la mitología y los nuevos dioses de la tecnología y los medios.",
                                new BigDecimal("55.00"), "EPUB",
                                "https://covers.openlibrary.org/b/id/12710000-M.jpg",
                                gaiman, fantasia);

                Libro nacidosBruma = crearLibro(
                                "El Imperio Final (Nacidos de la Bruma)",
                                "En un mundo donde la ceniza cae del cielo, un grupo de ladrones mágicos planea derrocar al Lord Legislador, un dios inmortal.",
                                new BigDecimal("58.00"), "EPUB",
                                "https://covers.openlibrary.org/b/id/14658420-M.jpg",
                                sanderson, fantasia);

                // --- Mas Ciencia Ficción ---
                Libro fundacion = crearLibro(
                                "Fundación",
                                "Hari Seldon inventa la psicohistoria para predecir la caída del Imperio Galáctico y establece la Fundación para preservar el conocimiento humano.",
                                new BigDecimal("45.00"), "PDF",
                                "https://covers.openlibrary.org/b/id/14560069-M.jpg",
                                asimov, cienciaFiccion);

                Libro dune = crearLibro(
                                "Dune",
                                "Paul Atreides viaja al peligroso planeta desértico Arrakis, la única fuente de la especia melange, la sustancia más valiosa del universo.",
                                new BigDecimal("65.00"), "EPUB",
                                "https://covers.openlibrary.org/b/id/14636513-M.jpg",
                                herbert, cienciaFiccion);

                Libro guiaGalactica = crearLibro(
                                "Guía del Autoestopista Galáctico",
                                "Segundos antes de que la Tierra sea demolida para hacer una autopista hiperespacial, Arthur Dent es rescatado por su amigo Ford Prefect, un investigador extraterrestre.",
                                new BigDecimal("38.00"), "EPUB",
                                "https://covers.openlibrary.org/b/id/15153298-M.jpg",
                                adams, cienciaFiccion);

                // --- Mas Desarrollo Personal ---
                Libro sapiens = crearLibro(
                                "Sapiens: De animales a dioses",
                                "Un breve viaje por la historia de la humanidad, desde los primeros homínidos hasta las revoluciones cognitiva, agrícola y científica.",
                                new BigDecimal("75.00"), "PDF",
                                "https://images.cdn2.buscalibre.com/fit-in/360x360/b5/1a/b51a9baa4e59e89a3578cb224e1f1d81.jpg",
                                harari, desarrolloPersonal);

                Libro comoGanarAmigos = crearLibro(
                                "Cómo ganar amigos e influir sobre las personas",
                                "Clásico atemporal sobre relaciones humanas, comunicación persuasiva y liderazgo positivo en entornos tanto personales como profesionales.",
                                new BigDecimal("42.00"), "EPUB",
                                "https://covers.openlibrary.org/b/id/13195013-M.jpg",
                                carnegie, desarrolloPersonal);

                // --- Clásicos ---
                Libro cienAnos = crearLibro(
                                "Cien Años de Soledad",
                                "La historia épica de la familia Buendía en el pueblo ficticio de Macondo, una obra maestra del realismo mágico.",
                                new BigDecimal("50.00"), "EPUB",
                                "https://covers.openlibrary.org/b/id/15219095-M.jpg",
                                marquez, clasicos);

                Libro rebelionGranja = crearLibro(
                                "Rebelión en la Granja",
                                "Los animales de una granja se rebelan contra su amo humano para crear una sociedad igualitaria, pero el poder pronto los corrompe.",
                                new BigDecimal("30.00"), "PDF",
                                "https://covers.openlibrary.org/b/id/13712713-M.jpg",
                                orwell, clasicos);

                // Guardamos todos los libros
                libroRepository.saveAll(List.of(
                                elSenorDeLosAnillos, elHobbit, harryPotter, nombreDelViento,
                                elCaminoDeLosReyes, milNovecientosOchentaYCuatro, cleanCode,
                                elMiticoHombresMes, habitosAtomicos, padrePadreRico, elCodigoDaVinci,
                                angelesYDemonios, elResplandor, it, diezNegritos, narnia, americanGods,
                                nacidosBruma, fundacion, dune, guiaGalactica, sapiens, comoGanarAmigos,
                                cienAnos, rebelionGranja));

                // ====================================================
                // 6. COMPRAS SIMULADAS (para poblar las analíticas)
                // Usamos los libros ya guardados para simular ventas
                // ====================================================
                List<Libro> librosGuardados = libroRepository.findAll();
                Libro libro1 = librosGuardados.get(0); // El Señor de los Anillos
                Libro libro2 = librosGuardados.get(1); // El Hobbit
                Libro libro3 = librosGuardados.get(2); // Harry Potter
                Libro libro4 = librosGuardados.get(6); // Clean Code
                Libro libro5 = librosGuardados.get(8); // Hábitos Atómicos
                Libro libro6 = librosGuardados.get(18); // Fundacion
                Libro libro7 = librosGuardados.get(19); // Dune
                Libro libro8 = librosGuardados.get(12); // El Resplandor
                Libro libro9 = librosGuardados.get(23); // Cien Años

                // === VENTAS DE HOY (aparecen en "periodo=hoy") ===
                compraRepository.save(crearCompra(cliente1, libro1, LocalDateTime.now().minusHours(1)));
                compraRepository.save(crearCompra(cliente2, libro1, LocalDateTime.now().minusHours(2)));
                compraRepository.save(crearCompra(cliente3, libro1, LocalDateTime.now().minusHours(3)));
                compraRepository.save(crearCompra(cliente1, libro3, LocalDateTime.now().minusHours(2)));
                compraRepository.save(crearCompra(cliente2, libro4, LocalDateTime.now().minusHours(1)));
                compraRepository.save(crearCompra(cliente3, libro5, LocalDateTime.now().minusHours(4)));
                compraRepository.save(crearCompra(cliente1, libro6, LocalDateTime.now().minusMinutes(30)));
                compraRepository.save(crearCompra(cliente2, libro7, LocalDateTime.now().minusMinutes(45)));
                compraRepository.save(crearCompra(cliente3, libro8, LocalDateTime.now().minusHours(5)));
                compraRepository.save(crearCompra(cliente1, libro9, LocalDateTime.now().minusHours(6)));
                compraRepository.save(crearCompra(cliente2, libro9, LocalDateTime.now().minusHours(2)));

                // === VENTAS DE LA SEMANA (aparecen en "periodo=semana") ===
                compraRepository.save(crearCompra(cliente1, libro2, LocalDateTime.now().minusDays(2)));
                compraRepository.save(crearCompra(cliente2, libro2, LocalDateTime.now().minusDays(3)));
                compraRepository.save(crearCompra(cliente3, libro2, LocalDateTime.now().minusDays(4)));
                compraRepository.save(crearCompra(cliente1, libro4, LocalDateTime.now().minusDays(2)));
                compraRepository.save(crearCompra(cliente2, libro4, LocalDateTime.now().minusDays(3)));
                compraRepository.save(crearCompra(cliente3, libro3, LocalDateTime.now().minusDays(5)));
                compraRepository.save(crearCompra(cliente1, libro1, LocalDateTime.now().minusDays(4)));
                compraRepository.save(crearCompra(cliente2, libro6, LocalDateTime.now().minusDays(1)));
                compraRepository.save(crearCompra(cliente3, libro6, LocalDateTime.now().minusDays(6)));
                compraRepository.save(crearCompra(cliente1, libro7, LocalDateTime.now().minusDays(3)));
                compraRepository.save(crearCompra(cliente2, libro8, LocalDateTime.now().minusDays(5)));

                // === VENTAS HISTÓRICAS (aparecen en "periodo=siempre") ===
                compraRepository.save(crearCompra(cliente1, libro1, LocalDateTime.now().minusMonths(2)));
                compraRepository.save(crearCompra(cliente2, libro1, LocalDateTime.now().minusMonths(3)));
                compraRepository.save(crearCompra(cliente3, libro4, LocalDateTime.now().minusMonths(1)));
                compraRepository.save(crearCompra(cliente1, libro5, LocalDateTime.now().minusMonths(2)));
                compraRepository.save(crearCompra(cliente2, libro3, LocalDateTime.now().minusMonths(4)));
                compraRepository.save(crearCompra(cliente3, libro2, LocalDateTime.now().minusMonths(3)));
                compraRepository.save(crearCompra(cliente1, libro4, LocalDateTime.now().minusMonths(1)));
                compraRepository.save(crearCompra(cliente2, libro5, LocalDateTime.now().minusMonths(5)));
                compraRepository.save(crearCompra(cliente3, libro6, LocalDateTime.now().minusMonths(6)));
                compraRepository.save(crearCompra(cliente1, libro6, LocalDateTime.now().minusMonths(8)));
                compraRepository.save(crearCompra(cliente2, libro7, LocalDateTime.now().minusMonths(2)));
                compraRepository.save(crearCompra(cliente3, libro8, LocalDateTime.now().minusMonths(7)));
                compraRepository.save(crearCompra(cliente1, libro9, LocalDateTime.now().minusMonths(12)));
                compraRepository.save(crearCompra(cliente2, libro9, LocalDateTime.now().minusMonths(10)));

                log.info("Siembra completada: {} categorias, {} autores, {} libros, {} compras simuladas.",
                                categoriaRepository.count(),
                                autorRepository.count(),
                                libroRepository.count(),
                                compraRepository.count());
        }

        // Método auxiliar para crear un libro con menos código repetido
        private Libro crearLibro(String titulo, String descripcion, BigDecimal precio,
                        String formato, String portadaUrl, Autor autor, Categoria categoria) {
                return Libro.builder()
                                .titulo(titulo)
                                .descripcion(descripcion)
                                .precio(precio)
                                .formato(formato)
                                .portadaUrl(portadaUrl)
                                .autor(autor)
                                .categoria(categoria)
                                .build();
        }

        // Método auxiliar para crear una compra completada con fecha específica
        private Compra crearCompra(Usuario usuario, Libro libro, LocalDateTime fecha) {
                return Compra.builder()
                                .usuario(usuario)
                                .libro(libro)
                                .monto(libro.getPrecio())
                                .estado("COMPLETADA")
                                .fechaCompra(fecha)
                                .build();
        }
}
