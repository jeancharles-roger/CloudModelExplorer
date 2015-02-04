# Project

org.xid.explorer.CloudModelExplorer is a project that aim be a flexible and a scalable model explorer.

[![Build Status](https://drone.io/github.com/jeancharles-roger/CloudModelExplorer/status.png)](https://drone.io/github.com/jeancharles-roger/CloudModelExplorer/latest)

## The project

I wan’t to build a piece of software that is able to explore a model to check for properties. It will feature:

- support for languages extensions (model kinds) with out translation.
- support for diagnostic analysis extensions.
- cloud platform support to benefit from vast resources.
- complete batch-able API (over REST probably).
- HTML5 base UI to present results.
- complete open-source code ([GitHub Project page](https://github.com/jeancharles-roger/org.xid.explorer.CloudModelExplorer "GitHub Project page"))
There are some other features I have in mind but it’ll come in time.

The explorer will be implemented using Java SE 8. The cloud platform will probably by OpenStack.

## A bit of context please.

Dynamic exploration is the exploration of all possible executions of a model. It’s mainly used in model-checking technics ([Model Checking on Wikipedia](http://en.wikipedia.org/wiki/Model_checking "Model Checking on Wikipedia"), checks the last section for more references).

A model here is a representation of a software. I wan’t to focus on conception models for embedded and critical systems. Complete exploration of implementation models or code is still an active research field and a difficult frontier to cross. On the other hand, the conception models are smaller and their exploration is accessible if we focus on relevant scenarios and if leverage cloud computing for the task.

Dynamic exploration allows to verify properties on the model behavior. The properties I wan’t to focus on (for starter) are invariants. Invariants are constant properties that must be true in all states. A simple example is that a lift must always move with closed doors.

## Why

This is an interesting question. I have worked in the model-checking field for 7 years at ENSTA-Bretagne (here is the OBP tool [web page](http://www.obpcdl.org/ "OBP-CDL")). I started with a Phd Thesis in 2003 and worked as a research engineer in a model-checking team. I’ve built there a solid experience on software verification. I also worked for 4 years for tools editors. I experienced there my tool building skills.

I’m convinced that a flexible and scalable model explorer can bring to the embedded software industry a significant asset to build more reliable softwares. I know that I can build the core of such a tool. I’m only doing this on my free time so I can’t build it quickly. I’m sharing my progress and my goals to allow other person to contribute if they are interested. These are the reasons why I’m building it as an open-source project and with the associated blog.

If you are interested by the project or have any question, please feel free to contact me on: `jeancharles.roger _AT_ gmail _DOT_ com`.

## Links
Here are some links concerning the project:

- You’ll find here the [Project Description](http://minibilles.fr/cloud-model-explorer/ "Project Description").
- You'll find here the [Blog Posts](http://minibilles.fr/category/cloudmodelexplorer/ "Blog Posts").
- You’ll find here the [GitHub Project page](https://github.com/jeancharles-roger/org.xid.explorer.CloudModelExplorer "GitHub Project page").

## References
Here are some references around exploration and model-checking. For most, they present some theoretical background and some practical uses cases.

Here are some Phd Thesis:

- [Jean-Charles Roger: Phd Thesis](http://minibilles.fr/wp-content/uploads/2014/12/theseRoger2006.pdf "Jean-Charles Roger Phd Thesis") (in french).
- [Patricia Bouyer: Phd Thesis](http://www.lsv.ens-cachan.fr/Publis/PAPERS/PDF/Bouyer-these.pdf "Patricia Bouyer Phd Thesis") (in french).

Here are some articles:

- [Philippe Dhaussy, Frédéric Boniol, Jean-Charles Roger and Luka Leroux: Improving Model Checking with Context Modelling](http://www.hindawi.com/journals/ase/2012/547157/ "Improving Model Checking with Context Modelling") (in english).
- [Gerd Behrmann, Johan Bengtsson, Alexandre David, Kim G. Larsen, Paul Pettersson, and Wang Yi: UPPAAL Implementation Secrets](http://people.cs.aau.dk/~adavid/publications/14-secrets.pdf "UPPAAL Implemantation secrets") (in english).
- [Stephan Merz: Model Checking: A tutorial view](http://www.loria.fr/~merz/papers/mc-tutorial.pdf "Model Checking: A tutorial view") (in english).

Here are some books:

- [Christel Baier and Joost-pieter Katoen: Principles of Model Checking](http://mitpress.mit.edu/books/principles-model-checking "Principles of Model Checking") on MIT Press (in english).
- [Edmund Clarke, Oma Grumberg and Doron Peled: Model Checking](http://mitpress.mit.edu/books/model-checking "Model Checking") on MIT Press (in english).

Here are some lecture notes:

- [Sébastien Bardin: Introduction au Model Checking](http://sebastien.bardin.free.fr/MC-ENSTA.pdf "Sébastien Bardin: Introduction au Model Checking") (in french).
- [Edmund Clarke, Allen Emerson and Joseph Sifakis: Turing Lecture on Model Checking](http://delivery.acm.org/10.1145/1600000/1592781/p74-clarke.pdf?ip=109.190.67.200&id=1592781&acc=OPEN&key=4D4702B0C3E38B35%2E4D4702B0C3E38B35%2E4D4702B0C3E38B35%2E6D218144511F3437&CFID=464648722&CFTOKEN=19391023&__acm__=1418921083_78266c7be8cdd6e10a270a37d5002255 "Edmund Clarke, Allen Emerson and Joseph Sifakis: Turing Lecture on Model Checking") (in english).

Here are some tools:

- [OBP-CDL](http://www.obpcdl.org/ "OBP-CDL")
