FROM ubuntu:latest
LABEL authors="karde"

ENTRYPOINT ["top", "-b"]