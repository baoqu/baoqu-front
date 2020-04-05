all: docker-clean docker-run

docker: docker-clean docker-build docker-run

docker-clean:
	docker rm -f baoqu-front || echo "No baoqu-front image found"

docker-build:
	docker build -t baoqu/front -f docker/Dockerfile .

docker-run:
	docker run -it --name baoqu-front -p 3449:3449 -v ${PWD}:/app baoqu/front

.PHONY: dist

dist:
	# clean removes the index file, so we restore it before compiling
	lein clean && git checkout -- resources/public/index.html
	rm -rf dist
	(cd layout && npx gulp dist)
	mkdir dist
	cp -r resources/public dist/
	lein cljsbuild once dist
