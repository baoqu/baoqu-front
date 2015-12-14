FROM debian
MAINTAINER Miguel de la Cruz <mgdelacroix@gmail.com>

RUN apt-get update && \
    apt-get install -y \
                    vim \
                    tmux \
                    curl \
                    rlwrap \
                    openjdk-7-jdk

RUN curl https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein -o /usr/local/bin/lein && chmod +x /usr/local/bin/lein && lein upgrade

ENV LEIN_ROOT 1
WORKDIR /app
EXPOSE 3449

CMD ./scripts/figwheel
