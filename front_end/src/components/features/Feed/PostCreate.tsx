"use client"

import {
  useForm
} from "react-hook-form"
import {
  zodResolver
} from "@hookform/resolvers/zod"
import * as z from "zod"
import {
  Button
} from "@/components/ui/button"
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormMessage,
} from "@/components/ui/form"
import {
  Input
} from "@/components/ui/input"
import { Textarea } from "@/components/ui/textarea"


import { useUser } from "@/UserProvider";



const formSchema = z.object({
    userId: z.number(),
    postContent: z.string().max(280, "Post content must be at most 280 characters"),
    productId: z.number(),
    files: z.array(z.instanceof(File)).optional()
});

export default function PostCreate({}) {
    const {user} = useUser();

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            userId: user.id,
            postContent: "",
            productId: 0,
            files: [],
        }
    })

    async function onSubmit(values: z.infer<typeof formSchema>) {
        try {
            const response = await fetch("http://localhost:8080/posts/create", {
                mode: "cors",
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(values),
            })
        } catch (error) {
            console.error("Error creating post", error)
        };
    }
    return (
        <Form {...form} >
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8 mx-auto py-8 w-5/6 mx-auto p-4 border rounded-lg shadow-md bg-white">
                <FormField
                    control={form.control}
                    name="postContent"
                    render={({ field }) => (
                    <FormItem>
                        <FormControl>
                            <Textarea 
                                placeholder="What's up with your new work?"
                                className="h-20"
                                {...field} 
                            />
                        </FormControl>
                        <FormMessage />
                    </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="files"
                    render={({ field }) => (
                        <FormItem>
                        <FormControl>
                            <Input
                            type="file"
                            accept="image/*"
                            multiple
                            onChange={(e) => field.onChange(e.target.files)}
                            />
                        </FormControl>
                        <FormDescription>Upload images for your post.</FormDescription>
                        <FormMessage />
                        </FormItem>
                    )}
                />
                    
            <Button type="submit">Post</Button>
            </form>
        </Form>
    )
}