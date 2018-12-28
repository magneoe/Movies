node {
    def app

    stage('Clone repository') {
        /* Let's make sure we have the repository cloned to our workspace */
	bat 'echo "Cloning repository"'
        checkout scm
    }
	
    stage('Build image') {
        /* This builds the actual image; synonymous to
         * docker build on the command line */
	bat 'echo "Building image"'
        bat 'docker build -t magneoe/movies:dev .'
    }

    stage('Test image') {
        /* Ideally, we would run a test framework against our image.
         * For this example, we're using a Volkswagen-type approach ;-) */
    }

    stage('Push image') {
        /* Finally, we'll push the image with two tags:
         * First, the incremental build number from Jenkins
         * Second, the 'latest' tag.
         * Pushing multiple tags is cheap, as all the layers are reused. */
        bat 'docker push magneoe/movies:dev'
    }
}
