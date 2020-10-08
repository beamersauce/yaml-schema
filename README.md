# YAML Schema

CLI that takes a YAML schema file and can validate a YAML data file, reporting any validation errors

```
yaml-schema schema.yml test.yaml
No errors found
```

## To Install

These are the current manual instructions (for Ubuntu via WSL at least - should work the same for most linux flavors) - I'll put out releases with instructions on how to install from there instead in the future

```
apt install openjdk-11-jre-headless 
gradle build
cp ./build/distributions/yaml-schema-0.0.1-SNAPSHOT.tar /opt/
tar -xvf yaml-schema-0.0.1-SNAPSHOT.tar
export PATH=$PATH:/opt/yaml-schema-0.0.1-SNAPSHOT/bin
yaml-schema schema.yml test.yaml
```


## How this works

Behind the scenes:

* convert YAML schema file to JSON
* convert YAML data file to JSON
* Use JSON schema to validate

will there be an issue with the error report referencing JSON locations/line #'s? TBD