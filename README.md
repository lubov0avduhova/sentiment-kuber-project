# Sentiment Kuber

Простое Spring Boot приложение для анализа тональности текста,
подготовленное для развёртывания в Minikube / Kubernetes.

## Запуск локально

```bash
mvn spring-boot:run
```

Тестовый запрос:

```bash
curl "http://localhost:8080/api/sentiment?text=Kubernetes+is+amazing+but+sometimes+slow"
```

## Docker

```bash
docker build -t sentiment-app:1.0.0 .
docker run -p 8080:8080 sentiment-app:1.0.0
```

## Kubernetes (Minikube)

```bash
kubectl apply -f k8s/k8s.yaml
kubectl get pods
kubectl get svc
```

Потом:

```bash
minikube service sentiment-service --url
```
