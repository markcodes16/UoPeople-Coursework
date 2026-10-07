# University of the People — Computer Science Coursework

Java and web development projects from my Computer Science studies at University of the People. This collection demonstrates object-oriented programming, desktop interfaces, concurrency, data processing, and web fundamentals.

## Featured projects

### [Employee Data Processor](java/employee-data-processor/)

A Java Swing application for filtering employee records, grouping employees by department, and calculating salary averages.

**Concepts:** Java Streams, lambda expressions, collections, CSV input, and separation of data processing from the interface.

### [Student Management GUI](java/student-management-gui/)

A desktop application for managing student records, course enrollment, and grades.

**Concepts:** Swing event handling, tables and dialogs, input validation, and relationships modeled with lists and maps.

### [Multithreaded Clock](java/multithreaded-clock/)

A clock with separate threads for updating and displaying the time.

**Concepts:** Runnable tasks, synchronized access to shared state, thread interruption, and shutdown handling.

## More Java projects

| Project | What it demonstrates |
| --- | --- |
| [Student and Course Management CLI](java/student-course-cli/) | Object modeling, enrollment management, grade calculations, and collections |
| [Vehicle Interfaces](java/vehicle-interfaces/) | Interfaces and polymorphism across car, motorcycle, and truck models |
| [Text Analysis Tool](java/text-analysis/) | Character and word frequency analysis using maps and sets |
| [Library Management](java/library-management/) | A console menu for managing books and borrowing |
| [Stock Price Analysis](java/stock-price-analysis/) | Arrays, lists, averages, maximum values, and cumulative sums |
| [Console Quiz](java/quiz/) | User input, branching, and score tracking |
| [Weather App](java/weather-app/) | JavaFX, HTTP API requests, JSON parsing with Gson, and local search history |

**Weather App status:** Incomplete. The required FXML, stylesheet, and build configuration are missing. The project README describes its dependencies and API key setup.

## Web projects

| Project | What it demonstrates |
| --- | --- |
| [Bookstore Data Interchange](web/bookstore-data-interchange/) | Representing catalog data in JSON and XML, defining an XSD schema, and rendering an inventory with JavaScript |
| [Temperature Converter](web/temperature-converter/) | JavaScript functions, input validation, and DOM updates |
| [To-Do List](web/to-do-list/) | DOM manipulation and event handling for adding, completing, and removing tasks |
| [Travel Blog](web/travel-blog/) | HTML page structure, CSS styling, and navigation across pages |

## Exploring the code

Each folder is an independent coursework project. Java source is under `src/`; each project's README identifies its main class. Compile projects separately with a compatible JDK. Swing applications require a desktop environment.

For web projects, open the HTML entry point in a browser: `index.html` for the first three projects and `travel_blog_homepage.html` for the Travel Blog.
