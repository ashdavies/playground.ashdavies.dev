interface Blob extends GitObject {
    data: {
        text: string
    }
}

interface GitObject {
    oid: string
}

interface Repository {
    repository: {
        conferences: GitObject & {
            entries: Array<Blob & TreeEntry>
        }
    }
}

interface TreeEntry extends GitObject {
    name: string
}

interface Query<T> {
    data: T
}