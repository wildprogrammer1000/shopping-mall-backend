docker volume create maria
docker image prune -f 
docker volume prune -f
docker container prune -f
docker compose up --build