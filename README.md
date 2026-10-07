# University of the People — Computer Science Coursework

A portfolio of programming work from my Computer Science studies at University of the People.

This repository contains **14 recovered projects: 10 Java projects and 4 web projects**, with **46 original source and data files**. The files were moved from a private recovery archive on 2026-10-07. Source file contents, filenames, package directories, and the earlier Travel Blog version are preserved.

## Java projects

| Project | Source files | Recovery status |
| --- | ---: | --- |
| [Console Quiz](java/quiz/) | 1 | Recovered source |
| [Library Management System](java/library-management/) | 1 | Recovered source |
| [Stock Price Analysis](java/stock-price-analysis/) | 1 | Recovered source |
| [Student and Course Management CLI](java/student-course-cli/) | 4 | Recovered source |
| [Vehicle Interfaces](java/vehicle-interfaces/) | 8 | Recovered source |
| [Student Management GUI](java/student-management-gui/) | 3 | Recovered source |
| [Employee Data Processor GUI](java/employee-data-processor/) | 3 | Recovered source |
| [Text Analysis Tool](java/text-analysis/) | 1 | Recovered source |
| [Multithreaded Clock](java/multithreaded-clock/) | 4 | Recovered source |
| [JavaFX Weather App](java/weather-app/) | 11 | Partial recovery; missing assets and build configuration |

## Web projects

| Project | Source and data files | Recovery status |
| --- | ---: | --- |
| [Temperature Converter](web/temperature-converter/) | 1 | Recovered source |
| [To-Do List](web/to-do-list/) | 1 | Recovered source |
| [Bookstore Data Interchange](web/bookstore-data-interchange/) | 4 | Recovered source |
| [Travel Blog](web/travel-blog/) | 3 | Recovered HTML, including earlier version |

## Getting started

Each project has its own README. Java projects are independent: compile each project's `src/` directory separately with a compatible JDK, then run its documented main class. Desktop GUIs require a graphical environment. Web projects can be opened using their HTML entry points; see the project README for filenames.

The JavaFX Weather App is an incomplete recovery. Its referenced `weather_app.fxml`, `styles.css`, and original build configuration were not located. It imports JavaFX and Gson, and reads `OPENWEATHER_API_KEY` from the environment. Those missing assets and dependencies must be supplied before it can run.

## Technical themes

Java, object-oriented programming, interfaces, multithreading, desktop GUIs, HTML, CSS, JavaScript, XML, JSON, and data processing.

## Transfer and verification

- All 46 source and data files are preserved byte for byte. Original Git blob hashes and path mappings are recorded in [TRANSFER_MANIFEST.json](TRANSFER_MANIFEST.json).
- The 14 project READMEs were carried over; 13 were updated to remove private document links and references to included submission transcripts.
- Full `submission.txt` transcripts and private recovery metadata remain in the private archive. Credentials, personal documents, and unrelated projects are excluded.
- Transfer verification checks file inventory, content hashes, and exclusions. It does not establish that every recovered application builds or runs; application behavior remains unverified.

This repository is a record of my learning and project work.
