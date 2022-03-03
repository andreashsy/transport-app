module.exports = [
  {
    context: ['/api/**', '/secure/**'],   // paths to forward
    target: 'http://localhost:8080',
    secure: false,         // https or not
    logLevel: 'debug'
  }
]
