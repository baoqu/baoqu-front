all: docker-clean docker-run

layout: docker-clean docker-run-background docker-att-layout

docker: docker-clean docker-build docker-run docker-att

docker-clean:
	docker rm -f baoqu-front || echo "No baoqu-front image found"

docker-build:
	docker build -t baoqu/front -f docker/Dockerfile .

docker-run:
	docker run -it --name baoqu-front -p 3449:3449 -v ${PWD}:/app baoqu/front

docker-run-background:
	docker run -d --name baoqu-front -p 3449:3449 -v ${PWD}:/app baoqu/front

docker-att-layout:
	docker exec -it baoqu-front "cd layout && gulp"
