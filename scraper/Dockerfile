FROM python:3.12-slim as base

WORKDIR /usr/src/app

RUN pip install --upgrade pip
RUN pip install --no-cache-dir poetry

COPY . .

FROM base as dev

ENV PATH=$PATH:/etc/poetry/bin

RUN apt -y update && apt -y upgrade 
RUN apt install -y --no-install-recommends net-tools iproute2 iputils-ping openssh-client npm git
RUN apt autoclean

FROM base as release

RUN poetry config virtualenvs.create false && poetry install
RUN playwright install && playwright install-deps

CMD [ "python", "-m", "scraper" ]