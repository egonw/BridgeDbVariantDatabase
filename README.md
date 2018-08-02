# BridgeDbVariantDatabase
Create a BridgeDb Gene-Variant database from Ensembl

* Read the source file, map the transcript identifier to gene identifier, and create the input .txt file for VariantCreator
* Specify the subset you want in the configuration file at the entry "subset"

VariantReader [Ensembl.vcf] [SNP.config]

  - You can find the vcf source file here: [homo_sapiens_incl_consequences.vcf.gz](//ftp.ensembl.org/pub/current_variation/vcf/)

* Build the BridgeDb Gene-Variant database, take a .config file as input

VariantCreator [SNP.config]
