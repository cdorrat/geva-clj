# geva-clj

## Features
geva-clj is a refactored version of the [GEVA gramatical evolution library](http://ncra.ucd.ie/Site/GEVA.html).
This version is based on GEVA v1.2 and adds [clojure support](http://geva-clj.github.com/xxx.html).

In addition to the clojure suuport the main changes from the GEVA release were:
- Change the build to use Maven
- Change all System.out.println references to use loggers
- Split the source into core, examples and gui projects
- Add support for evaluating populations as a group (useful for running fitness functions on a cluster)
 

## Documentation and Source

- A getting started guide is available at [http://cdorrat.github.com/geva-clj/](http://cdorrat.github.com/geva-clj/)
- Documentation and the source are available on GitHib at [http://github.com/cdorrat/geva-clj](http://github.com/cdorrat/geva-clj)


## Usage
The fastest way to use this library is with Leiningen . Add the following to your project.clj dependencies:

```clojure
[org.clojars.cdorrat/geva-core "1.2-SNAPSHOT"]
```

## License

Distributed under the GNU general public license version 3, the same as GEVA.
