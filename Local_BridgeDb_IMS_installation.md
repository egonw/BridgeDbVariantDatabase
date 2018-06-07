# Tutorial - how to install and load IMS with data needed for gene-to-variant and variant-to-gene

This tutorial explains how to install and run a BridgeDb Identifier Mapping Service (IMS)
with gene-to-variant and variant-to-gene functionality.

1. Downloading the gene-variant mappings

Download the linksets data you want to load into the IMS from http://bridgedb.org/data/linksets/current/.

2. Downloading the BridgeDb IMS Docker

Pull the IMS docker image:

```shell
docker pull openphacts/identitymappingservice
```

