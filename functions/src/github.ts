interface GitHubObject {
    data: {
        text: string
    }
}

interface Repository {
    repository: {
        conferences: {
            entries: Array<TreeEntry & GitHubObject>
        }
    }
}

interface TreeEntry {
    name: string
    oid: string
}

interface Query<T> {
    data: T
}