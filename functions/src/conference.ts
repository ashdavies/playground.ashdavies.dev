interface Conference {
    name: string
    location: string
    website: string
    dateStart: Date
    dateEnd: Date
    online: boolean
    status: string
    cfp: Cfp
}

interface Cfp {
    start: Date
    end: Date
    site: string
}