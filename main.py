import http.server
import redis

redis_conn = redis.Redis(host='redis', port=6379)


class RequestHandler(http.server.BaseHTTPRequestHandler):
    def input(self):
        self.send_response(200)
        self.send_header('Content-type', 'text/plain')
        self.end_headers()

        if self.path == '/':
            redis_conn.incr('counter')
            self.wfile.write(b'This page was visited %s times.' % redis_conn.get('counter'))


server = http.server.HTTPServer(('localhost', 8080), RequestHandler)

server.serve_forever()