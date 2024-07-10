const express = require("express")

const app = express()

app.use(express.json())

app.post('/rest/api/3/issue', (req, res) => {
    const body = req.body

    console.log("RECEIVED NEW ORDER REQUEST", req.headers, JSON.stringify(body, null, 2))

    const title = body?.fields?.title ?? "Title not available"
    res.json({

    })
})


process.on('SIGINT', () => {
    console.info("Interrupted (SIGINT)");
    process.exit(0);
})
process.on('SIGTERM', () => {
    console.info("Interrupted (SIGTERM)");
    process.exit(0);
})

app.listen(8070)
console.log("Listening in 8070")



