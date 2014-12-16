# Project

CloudModelExplorer is a project that aim be a flexible and a scalable model explorer.

Links:
- You’ll find here the [Project Description](http://minibilles.fr/ "Project Description").
- You’ll find here the [GitHub Project page](https://github.com/jeancharles-roger/CloudModelExplorer "GitHub Project page").


## The project
I wan’t to build a piece of software that is able to explore a model to check for properties. It will feature:
- support for languages extensions (model kinds) with out translation.
- support for diagnostic analysis extensions.
- cloud platform support to benefit from vast resources.
- complete batch-able API (over REST probably).
- HTML5 base UI to present results.
- complete open-source code (**TODO** Add link to GitHub)
There are some other features I have in mind but it’ll come in time.

The explorer will be implemented using Java SE 8. The cloud platform will probably by OpenStack.

## A bit of context please.

Dynamic exploration is the exploration of all possible executions of a model. It’s mainly used in model-checking techniques (**TODO** add references, wikipedia for definitions, and some ground based research articles, maybe books…).

A model here is a representation of a software. I wan’t to focus on conception models for embedded and critical systems. Complete exploration of implementation models or code is still an active research field and a difficult frontier to cross. On the other hand, the conception models are smaller and their exploration is accessible if we focus on relevant scenarios and if leverage cloud cloud computing for the task.

Dynamic exploration allows to verify properties on the model behavior. The properties I wan’t to focus on (for starter) are invariants. Invariants are constant properties that must be true in all states. A simple example is that a lift must always move with closed doors.

## Why

This is an interesting question. I have worked in the model-checking field for 7 years. I started with a Phd Thesis in 2003 and worked as a research engineer in a model-checking team. I’ve built there a solid experience on software verification. I also worked for 4 years for tools editors. I experienced there my tool building skills.

I’m convinced that a flexible and scalable model explorer can bring to the embedded software industry a significant asset to build more reliable softwares. I know that I can build the core of such a tool. I’m only doing this on my free time so I can’t build it quickly. I’m sharing my progress and my goals to allow other person to contribute if they are interested. These are the reasons why I’m building it as an open-source project and with the associated blog.

If you are interested to participate please contact me **TODO**.

## References

**TODO**
