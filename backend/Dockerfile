FROM python:3.8-slim
LABEL maintainer="aisdos007@gmail.com"

ENV PROJECT_ROOT /app
WORKDIR $PROJECT_ROOT

COPY backend_requirements.txt requirements.txt
RUN pip install -r requirements.txt

COPY . .
CMD python manage.py runserver 0.0.0.0:8000