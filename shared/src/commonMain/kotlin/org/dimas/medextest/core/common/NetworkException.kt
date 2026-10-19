package org.dimas.medextest.core.common

class NetworkException(endpoint: String) : Exception("Mock network failure: $endpoint")
