---
layout: default

trainingMaterial:
  "@context": http://schema.org/
  "@type": CreativeWork
  audience:
    - "@type": Audience
      name: PhD students
    - "@type": Audience
      name: post-docs
  genre:
    - "@type": URL
      url: http://edamontology.org/topic_0091 # Bioinformatics
  name: Browsing the eNanoMapper ontology with BioPortal, AberOWL and Protégé
  author:
    - "@type": Person
      name: Jonathan Mélius
      identifier: 0000-0001-8624-2972
    - "@type": Person
      name: Egon Willighagen
      identifier: 0000-0001-7542-0286
    - "@type": Person
      name: Friederike Ehrhart
      identifier: 0000-0002-7770-620X
  difficultyLevel: [Beginner]
  keywords: BridgeDb, gene, variant, SNP, identifier
  license: CC-BY 4.0
  url:
    - "@type": URL
      url: https://enanomapper.github.io/tutorials/BrowseOntology/Tutorial%20browsing%20eNM%20ontology.html
  version: 1.0
---

# Tutorial - how to install and load IMS with data needed for gene-to-variant and variant-to-gene

This tutorial explains how to install and run a BridgeDb Identifier Mapping Service (IMS)
with gene-to-variant and variant-to-gene functionality.

1. Downloading the gene-variant mappings
  
  Download the linksets data you want to load into the IMS from http://bridgedb.org/data/gene_database/linkset/.
  
2. Install Docker

  A prerequisite is a Docker installation. You can download Docker from various places and many GNU/Linux distributions
  ship a version as part of their distribution. Other options include:
  
  * Docker Community Edition: https://www.docker.com/get-docker
  * Docker Toolbox for older Mac and Windows versions: https://docs.docker.com/toolbox/overview/
  * Docker for Windows: https://docs.docker.com/docker-for-windows/
  
3. Downloading the BridgeDb IMS Docker
  
  Pull the IMS docker image:

  ```shell
  docker pull openphacts/identitymappingservice
  ```

