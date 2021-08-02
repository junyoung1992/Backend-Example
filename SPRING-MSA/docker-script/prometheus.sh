docker run -d \
  -p 9090:9090 \
  --network ecommerce-network \
  --name prometheus \
  -v /Users/orsay0827/Projects/prometheus-2.28.1.darwin-amd64/prometheus.yml:/etc/prometheus/prometheus.yml \
  prom/prometheus
