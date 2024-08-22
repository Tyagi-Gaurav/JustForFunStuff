import {
  ApolloClient,
  InMemoryCache,
  gql,
} from "@apollo/client";

export const apolloClient = new ApolloClient({
  uri: "/admin/graphql",
  cache: new InMemoryCache(),
});

export const getWordsGraphQL = (pageNum) => {
    return apolloClient.query({
        query: gql`
            query GetAllWords {
                allWords(pageNum: ${pageNum}) {
                    words {
                        word
                        meanings {
                            definition
                            synonyms
                            examples
                        }
                    }
                    totalWords
                    totalPages
                    currentPage
                    nextPage
                    previousPage
                }
            }`,
    })
  };
