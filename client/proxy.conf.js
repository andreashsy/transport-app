module.exports = [
  {
    context: ['/api/**'],   // paths to forward
    target: 'http://localhost:8080',
    secure: false,         // https or not
    logLevel: 'debug'
  }
]
