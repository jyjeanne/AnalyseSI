# AnalyseSI

[![License: GPL v2](https://img.shields.io/badge/License-GPL%20v2-blue.svg)](https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html)
[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.java.com)
[![Maven](https://img.shields.io/badge/Maven-3.6%2B-C71A36.svg)](https://maven.apache.org/)
[![CI/CD](https://github.com/analysesi/analysesi/workflows/CI%2FCD%20Pipeline/badge.svg)](https://github.com/analysesi/analysesi/actions)

## Description

**AnalyseSI** is a Java Entity Relationship Model application based on the Merise method.

AnalyseSI vous permet de modéliser votre base de données à l'aide de la méthode MERISE. C'est un outil complet pour la conception et la modélisation de bases de données relationnelles, offrant une interface graphique intuitive pour créer des MCD (Modèle Conceptuel de Données) et générer automatiquement les MLD (Modèle Logique de Données) et scripts SQL correspondants.

## 🚀 Features

- **Modélisation MCD** : Création graphique de modèles conceptuels de données
- **Génération automatique MLD** : Transformation automatique du MCD en modèle logique
- **Export SQL** : Génération de scripts SQL pour différents SGBD (MySQL, PostgreSQL, Oracle)
- **Interface graphique intuitive** : Interface Swing moderne et ergonomique
- **Gestion des cardinalités** : Support complet des cardinalités Merise
- **Dictionnaire de données** : Gestion centralisée des attributs et types de données
- **Import/Export** : Sauvegarde des projets au format .asi
- **Multi-plateforme** : Compatible Windows, Linux, macOS

## 📋 Requirements

- Java 17 or higher
- Maven 3.6 or higher (for building from source)

## 🔧 Installation

### Option 1: Download Pre-built JAR

Download the latest release from the [Releases page](https://github.com/analysesi/analysesi/releases)

```bash
java -jar analyse-si-1.0-SNAPSHOT.jar
```

### Option 2: Build from Source

```bash
# Clone the repository
git clone https://github.com/analysesi/analysesi.git
cd analysesi

# Build with Maven
mvn clean package

# Run the application
java -jar target/analyse-si-1.0-SNAPSHOT.jar
```

## 💻 Usage

1. **Créer un nouveau projet** : File → New Project
2. **Ajouter des entités** : Utilisez la barre d'outils pour ajouter des entités
3. **Définir les attributs** : Double-cliquez sur une entité pour définir ses attributs
4. **Créer des associations** : Reliez les entités avec des associations
5. **Générer le MLD** : Tools → Generate MLD
6. **Exporter en SQL** : File → Export → SQL Script

## 🛠️ Development

### Project Structure

```
analysesi/
├── src/
│   ├── main/
│   │   ├── java/         # Source code
│   │   └── resources/    # Resources (images, properties)
│   └── test/
│       └── java/         # Unit tests
├── .github/
│   └── workflows/        # CI/CD configuration
├── pom.xml              # Maven configuration
└── README.md            # This file
```

### Building and Testing

```bash
# Compile the project
mvn compile

# Run tests
mvn test

# Package the application
mvn package

# Run with Maven
mvn exec:java -Dexec.mainClass="org.analyse.main.Main"
```

## 📚 Dependencies

| Library | Version | Purpose |
|---------|---------|---------|
| JGoodies Forms | 1.2.1 | Advanced form layout management |
| JGoodies Looks | 2.7.0 | Professional Swing look and feel |
| JUnit Jupiter | 5.10.1 | Unit testing framework |
| Mockito | 5.8.0 | Mocking framework for tests |

## 👥 Contributors

See [CONTRIBUTORS.md](CONTRIBUTORS.md) for a list of contributors to this project.

### Original Author
- **Bruno Dabo** - Initial work and project creation

### Main Contributors
- **Loic Dreux** - Core development
- **Jean-Baptiste** - Refactoring and modernization

## 📬 Community & Support

- **Website**: [http://www.analysesi.com](http://www.analysesi.com)
- **Mailing List**: analysesi@lists.launchpad.net
- **Twitter**: [@analysesi](http://twitter.com/analysesi)
- **Launchpad**: [https://launchpad.net/analysesi](https://launchpad.net/analysesi)
- **Issues**: [GitHub Issues](https://github.com/analysesi/analysesi/issues)

## 📝 License

This project is licensed under the GNU General Public License v2.0 - see the [LICENSE](LICENSE) file for details.

```
AnalyseSI - Entity Relationship Model application
Copyright (C) 2003-2024 AnalyseSI Contributors

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.
```

## 🤝 Contributing

Contributions are welcome! Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct and the process for submitting pull requests.

### How to Contribute

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 🏗️ Roadmap

- [ ] Migration to JavaFX for modern UI
- [ ] Cloud synchronization support
- [ ] Export to additional database formats
- [ ] Reverse engineering from existing databases
- [ ] Collaborative editing features
- [ ] Docker containerization
- [ ] REST API for integration

## 📈 Project Status

- **Current Version**: 0.80 - Cairns
- **Status**: Active Development
- **Next Release**: Planning for v1.0

## 🙏 Acknowledgments

- The Merise methodology creators
- JGoodies for excellent Swing components
- All contributors and users of AnalyseSI
- The open source community

---

<p align="center">
  Made with ❤️ by the AnalyseSI Community
</p>

<p align="center">
  <a href="http://www.analysesi.com">Website</a> •
  <a href="https://github.com/analysesi/analysesi/wiki">Wiki</a> •
  <a href="https://github.com/analysesi/analysesi/issues">Report Bug</a> •
  <a href="https://github.com/analysesi/analysesi/issues">Request Feature</a>
</p>