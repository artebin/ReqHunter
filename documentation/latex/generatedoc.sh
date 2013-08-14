#!/bin/bash

# this script compiles the LaTeX documentation and generates the HTML pages.

pdflatex document.tex
latex2html document.tex -dir ../html
